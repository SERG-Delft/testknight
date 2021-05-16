package com.testbuddy.services

import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDeclarationStatement
import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiThisExpression
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.utilities.StringFormatter
import com.testbuddy.models.ClassFieldMutationSideEffect
import com.testbuddy.models.MethodCallOnClassFieldSideEffect
import com.testbuddy.models.MethodCallOnParameterSideEffect
import com.testbuddy.models.ReassignsClassFieldSideEffect
import com.testbuddy.models.SideEffect

// I plan to refactor this class in the next sprint so for now
// I am suppressing the warning.
//@Suppress("TooManyFunctions")
class MethodAnalyzerService {

    /**
     * Gets the side effects of a given method. It currently
     * recognises exceptions and class field mutations as side-effects.
     *
     * @param method the PsiMethod to be analyzed.
     * @return a list of SideEffect objects based on the side-effects the method has.
     */
    fun getSideEffects(method: PsiMethod): List<SideEffect> {
        val classFieldReassignmentSideEffects = getClassFieldsAffected(method)
        val argumentMutationSideEffects = getArgumentsAffected(method)
        return classFieldReassignmentSideEffects + argumentMutationSideEffects
    }

    // method calls for detecting argument mutations
    /**
     * Finds all the arguments affected by the method.
     *
     * @param method the method to analyze.
     * @return a list of MethodCallOnParameterSideEffect objects representing the found side-effects.
     */
    private fun getArgumentsAffected(method: PsiMethod): List<MethodCallOnParameterSideEffect> {
        val parametersInMethodScope = getParametersOfMethod(method)

        val methodCallExpressions = PsiTreeUtil.findChildrenOfType(method, PsiMethodCallExpression::class.java)

        val parentClass = PsiTreeUtil.getParentOfType(method, PsiClass::class.java)
        val identifiersInClassScope = parentClass?.allFields?.map { it.name }?.toSet() ?: return emptyList()

        return methodCallExpressions.flatMap {
            getArgumentsAffectedByMethodCall(
                it,
                parametersInMethodScope,
                identifiersInClassScope
            )
        }
    }

    /**
     * Gets all the arguments affected by a method call.
     *
     * @param methodCall the method call to be analyzed.
     * @param parametersInMethodScope the parameters in scope of the method.
     * @param identifiersInClassScope the identifiers in class scope.
     */
    private fun getArgumentsAffectedByMethodCall(
        methodCall: PsiMethodCallExpression,
        parametersInMethodScope: Set<String>,
        identifiersInClassScope: Set<String>
    ): List<MethodCallOnParameterSideEffect> {
        val methodName = methodCall.methodExpression.qualifiedName
        val arguments = mutableListOf<String>()
        if (methodName.contains(".")) {
            val argumentAppliedOn = methodName.subSequence(0, methodName.lastIndexOf(".")) as String
            arguments.add(argumentAppliedOn)
        }
        methodCall.argumentList.expressions.forEach { arguments.add(it.text) }
        val argumentsThatAreMethodParameters = arguments.filter {
            !isClassField(it, parametersInMethodScope, identifiersInClassScope)
        }
        return argumentsThatAreMethodParameters.map {
            MethodCallOnParameterSideEffect(
                it,
                StringFormatter.formatMethodName(methodName)
            )
        }
    }

    // methods for detecting class field mutations

    /**
     * Finds all the class fields affected by the method.
     *
     * @param method the method to be analyzed.
     * @return a list of MutatesClassFieldSideEffect objects representing the class fields that change.
     */
    private fun getClassFieldsAffected(method: PsiMethod): List<ClassFieldMutationSideEffect> {
        val identifiersInMethodScope = getIdentifiersInMethodScope(method)
        val assignmentExpressions = PsiTreeUtil.findChildrenOfType(method, PsiAssignmentExpression::class.java)
        val methodCallExpressions = PsiTreeUtil.findChildrenOfType(method, PsiMethodCallExpression::class.java)

        val parentClass = PsiTreeUtil.getParentOfType(method, PsiClass::class.java)
        val identifiersInClassScope = parentClass?.allFields?.map { it.name }?.toSet() ?: return emptyList()

        val classFieldsAffectedByAssignments = assignmentExpressions.flatMap {
            getClassFieldsReassigned(
                it,
                identifiersInMethodScope,
                identifiersInClassScope
            )
        }

        val classFieldsAffectedByMethodCalls =
            methodCallExpressions.flatMap {
                getClassFieldsAffectedByMethodCall(
                    it,
                    identifiersInMethodScope,
                    identifiersInClassScope
                )
            }

        return classFieldsAffectedByAssignments + classFieldsAffectedByMethodCalls
    }

    /**
     * Checks whether the given assignment expression affects one of the class fields.
     *
     * @param assignment the PsiAssignmentExpression to be analyzed.
     * @param identifiersInMethodScope the identifiers that this method binds.
     *                        This is passed as an argument for optimisation purposes.
     * @return true iff the assignment affects a class field.
     */
    private fun affectsClassField(
        assignment: PsiAssignmentExpression,
        identifiersInMethodScope: Set<String>,
        identifiersInClassScope: Set<String>
    ): Boolean {
        val leftExpression = assignment.lExpression
        return if (leftExpression.firstChild is PsiThisExpression) {
            val nameAffected = (leftExpression as PsiReferenceExpression).qualifiedName.replaceFirst("this.", "")
            identifiersInClassScope.contains(nameAffected)
        } else {
            val nameAffected = (leftExpression as PsiReferenceExpression).qualifiedName
            !identifiersInMethodScope.contains(nameAffected) && identifiersInClassScope.contains(nameAffected)
        }
    }

    /**
     * Returns all the class fields re-assigned by an assignment.
     *
     * @param assignment the assignment.
     * @param identifiersInMethodScope the identifiers in the method scope.
     * @param identifiersInClassScope the identifiers in the method scope.
     * @return a list of ReassignsClassFieldSideEffect objects representing the reassignments.
     */
    private fun getClassFieldsReassigned(
        assignment: PsiAssignmentExpression,
        identifiersInMethodScope: Set<String>,
        identifiersInClassScope: Set<String>
    ): List<ReassignsClassFieldSideEffect> {
        return if (affectsClassField(assignment, identifiersInMethodScope, identifiersInClassScope)) {
            listOf(
                ReassignsClassFieldSideEffect(
                    StringFormatter.formatClassFieldName((assignment.lExpression as PsiReferenceExpression).qualifiedName)
                )
            )
        } else {
            emptyList()
        }
    }

    /**
     * Finds all the class fields affected by a method call.
     *
     * @param methodCall the method call.
     * @param identifiersInMethodScope the identifiers in the method scope.
     * @param identifiersInClassScope  the identifiers in the class scope.
     * @return a list of MethodCallOnClassFieldSideEffect objects representing the mutated fields.
     */
    private fun getClassFieldsAffectedByMethodCall(
        methodCall: PsiMethodCallExpression,
        identifiersInMethodScope: Set<String>,
        identifiersInClassScope: Set<String>
    ): List<MethodCallOnClassFieldSideEffect> {
        val methodName = methodCall.methodExpression.qualifiedName
        val arguments = mutableListOf<String>()
        if (methodName.contains(".")) {
            val argumentAppliedOn = methodName.subSequence(0, methodName.lastIndexOf(".")) as String
            arguments.add(argumentAppliedOn)
        }
        methodCall.argumentList.expressions.forEach { arguments.add(it.text) }
        val argumentsThatAreClassFields = arguments.filter {
            isClassField(it, identifiersInMethodScope, identifiersInClassScope)
        }
        return argumentsThatAreClassFields.map {
            MethodCallOnClassFieldSideEffect(
                StringFormatter.formatClassFieldName(it),
                StringFormatter.formatMethodName(methodName)
            )
        }
    }

    /**
     * Checks whether an identifier is a class field.
     *
     * @param identifier the name of the identifier.
     * @param identifiersInMethodScope the identifiers in the method scope.
     * @param identifiersInClassScope the identifiers in the class scope.
     * @return true iff identifier is a class field.
     */
    private fun isClassField(
        identifier: String,
        identifiersInMethodScope: Set<String>,
        identifiersInClassScope: Set<String>
    ): Boolean {
        return if (identifier.contains("this.")) {
            val newName = identifier.replaceFirst("this.", "")
            !identifiersInMethodScope.contains(newName) && identifiersInClassScope.contains(newName)
        } else {
            !identifiersInMethodScope.contains(identifier) && identifiersInClassScope.contains(identifier)
        }
    }

    // utility methods

    private fun getParametersOfMethod(method: PsiMethod): Set<String> {
        val result = mutableSetOf<String>()
        val parameterNames = PsiTreeUtil.findChildrenOfType(method, PsiParameter::class.java).map { it.name }
        for (name in parameterNames) {
            result.add(name)
        }
        return result
    }

    /**
     * Gets the name of all the identifiers that are defined in the scope of the method.
     *
     * @param method the PsiMethod to analyze.
     * @return a set containing the names of the identifiers.
     */
    private fun getIdentifiersInMethodScope(method: PsiMethod): Set<String> {
        val result = mutableSetOf<String>()
        val parameterNames = PsiTreeUtil.findChildrenOfType(method, PsiParameter::class.java).map { it.name }
        val declarationNames = PsiTreeUtil.findChildrenOfType(method, PsiDeclarationStatement::class.java)
            .mapNotNull { PsiTreeUtil.getChildOfType(it, PsiLocalVariable::class.java)?.name }
        for (name in parameterNames) {
            result.add(name)
        }
        for (name in declarationNames) {
            result.add(name)
        }
        return result
    }

}
