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
import com.testbuddy.com.testbuddy.models.sideEffectAnalysis.MethodCall
import com.testbuddy.models.ClassFieldMutationSideEffect
import com.testbuddy.models.MethodCallOnClassFieldSideEffect
import com.testbuddy.models.MethodCallOnParameterSideEffect
import com.testbuddy.models.ReassignsClassFieldSideEffect
import com.testbuddy.models.SideEffect
import com.testbuddy.utilities.StringFormatter

class MethodAnalyzerService {

    private val primitives = setOf(
        "byte", "short", "int", "long",
        "float", "double", "char", "boolean",
        "!UNKNOWN"
    )

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
        val identifiersInClassScope = getIdentifiersInClassScope(method)

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
     * @param methodCallExpression the method call to be analyzed.
     * @param parametersInMethodScope the parameters in scope of the method.
     * @param identifiersInClassScope the identifiers in class scope.
     */
    private fun getArgumentsAffectedByMethodCall(
        methodCallExpression: PsiMethodCallExpression,
        parametersInMethodScope: Map<String, String>,
        identifiersInClassScope: Map<String, String>,
    ): List<MethodCallOnParameterSideEffect> {
        val methodCall = MethodCall.create(methodCallExpression)
        val argumentsThatAreMethodParameters = methodCall.args.filter {
            !isClassField(it, parametersInMethodScope, identifiersInClassScope)
        }
        val argumentsWithReferenceTypes =
            argumentsThatAreMethodParameters.filter { !primitives.contains(identifiersInClassScope[it]) }
        return argumentsWithReferenceTypes.map {
            MethodCallOnParameterSideEffect(
                it,
                StringFormatter.formatMethodName(methodCall.name)
            )
        }
    }

    /**
     * Finds all the class fields affected by a method call.
     *
     * @param methodCallExpression the method call.
     * @param identifiersInMethodScope the identifiers in the method scope.
     * @param identifiersInClassScope  the identifiers in the class scope.
     * @return a list of MethodCallOnClassFieldSideEffect objects representing the mutated fields.
     */
    private fun getClassFieldsAffectedByMethodCall(
        methodCallExpression: PsiMethodCallExpression,
        identifiersInMethodScope: Map<String, String>,
        identifiersInClassScope: Map<String, String>
    ): List<MethodCallOnClassFieldSideEffect> {
        val methodCall = MethodCall.create(methodCallExpression)
        val argumentsThatAreClassFields = methodCall.args.filter {
            isClassField(it, identifiersInMethodScope, identifiersInClassScope)
        }
        val argumentsThatAreReferences = argumentsThatAreClassFields.filter { !primitives.contains(identifiersInClassScope[it]) }
        return argumentsThatAreReferences.map {
            MethodCallOnClassFieldSideEffect(
                StringFormatter.formatClassFieldName(it),
                StringFormatter.formatMethodName(methodCall.name)
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
        identifiersInMethodScope: Map<String, String>,
        identifiersInClassScope: Map<String, String>
    ): Boolean {
        return if (identifier.contains("this.")) {
            val newName = identifier.replaceFirst("this.", "")
            !identifiersInMethodScope.contains(newName) && identifiersInClassScope.contains(newName)
        } else {
            !identifiersInMethodScope.contains(identifier) && identifiersInClassScope.contains(identifier)
        }
    }

    // utility methods

    /**
     * Gets the name of all the identifiers that are defined in the scope of the method.
     *
     * @param method the PsiMethod to analyze.
     * @return a set containing the names of the identifiers.
     */
    private fun getIdentifiersInMethodScope(method: PsiMethod): Map<String,String> {
        val localVariablesOfMethod = getLocalVariablesOfMethod(method)
        val parametersOfMethod = getParametersOfMethod(method)
        val result = mutableMapOf<String, String>()
        localVariablesOfMethod.forEach { result[it.key] = it.value }
        parametersOfMethod.forEach { result[it.key] = it.value }
        return result
    }

    private fun getParametersOfMethod(method: PsiMethod): Map<String, String> {
        val result = mutableMapOf<String, String>()
        val parameters = PsiTreeUtil.findChildrenOfType(method, PsiParameter::class.java)
        for (parameter in parameters) {
            result[parameter.name] = parameter.type.canonicalText
        }
        return result
    }


    private fun getLocalVariablesOfMethod(method: PsiMethod): Map<String, String> {
        val result = mutableMapOf<String, String>()
        val declarations = PsiTreeUtil.findChildrenOfType(method, PsiDeclarationStatement::class.java)
            .mapNotNull { PsiTreeUtil.getChildOfType(it, PsiLocalVariable::class.java) }
        for (declaration in declarations) {
            result[declaration.name] = declaration.type.canonicalText
        }
        return result
    }

    private fun getIdentifiersInClassScope(method: PsiMethod): Map<String, String> {
        val parentClass = PsiTreeUtil.getParentOfType(method, PsiClass::class.java) ?: return emptyMap()
        val result = mutableMapOf<String, String>()
        parentClass.allFields.forEach {
            result[it.name] = it.type.canonicalText
        }
        result["this"] = parentClass.name ?: "!UNKNOWN"
        return result
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
        val identifiersInClassScope = getIdentifiersInClassScope(method)

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
        identifiersInMethodScope: Map<String, String>,
        identifiersInClassScope: Map<String, String>
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
        identifiersInMethodScope: Map<String, String>,
        identifiersInClassScope: Map<String, String>
    ): List<ReassignsClassFieldSideEffect> {
        return if (affectsClassField(assignment, identifiersInMethodScope, identifiersInClassScope)) {
            listOf(
                ReassignsClassFieldSideEffect(
                    StringFormatter.formatClassFieldName(
                        (assignment.lExpression as PsiReferenceExpression).qualifiedName
                    )
                )
            )
        } else {
            emptyList()
        }
    }

}
