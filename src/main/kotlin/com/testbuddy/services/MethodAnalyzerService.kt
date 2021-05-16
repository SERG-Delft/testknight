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
import com.testbuddy.com.testbuddy.models.ClassFieldMutationSideEffect
import com.testbuddy.com.testbuddy.models.MethodCallOnArgumentSideEffect
import com.testbuddy.com.testbuddy.models.MethodCallOnClassFieldSideEffect
import com.testbuddy.com.testbuddy.models.ReassignsClassFieldSideEffect
import com.testbuddy.com.testbuddy.models.SideEffect

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
        val classFieldMethodCallsSideEffects: List<ReassignsClassFieldSideEffect> = emptyList()
        val argumentMutationSideEffects: List<MethodCallOnArgumentSideEffect> = emptyList()
        return classFieldReassignmentSideEffects + classFieldMethodCallsSideEffects + argumentMutationSideEffects
    }

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
                classFieldsAffectedByMethodCall(
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

    private fun getClassFieldsReassigned(
        assignment: PsiAssignmentExpression,
        identifiersInMethodScope: Set<String>,
        identifiersInClassScope: Set<String>
    ): List<ReassignsClassFieldSideEffect> {
        return if (affectsClassField(assignment, identifiersInMethodScope, identifiersInClassScope)) {
            listOf(
                ReassignsClassFieldSideEffect(
                    formatClassFieldName((assignment.lExpression as PsiReferenceExpression).qualifiedName)
                )
            )
        } else {
            emptyList()
        }
    }

    private fun classFieldsAffectedByMethodCall(
        methodCall: PsiMethodCallExpression,
        identifiersInMethodScope: Set<String>,
        identifiersInClassScope: Set<String>
    ): List<MethodCallOnClassFieldSideEffect> {
        val methodName = methodCall.methodExpression.qualifiedName
        //if it contains
        val arguments = mutableListOf<String>()
        if (methodName.contains(".")) {
            val argumentAppliedOn = methodName.subSequence(0, methodName.lastIndexOf(".")) as String
            arguments.add(argumentAppliedOn)
        }
        methodCall.argumentList.expressions.forEach { arguments.add(it.text) }
        val argumentsThatAffectClassFields = arguments.filter {
            isClassField(it, identifiersInMethodScope, identifiersInClassScope)
        }
        return argumentsThatAffectClassFields.map {
            MethodCallOnClassFieldSideEffect(
                formatClassFieldName(it),
                formatMethodName(methodName)
            )
        }
    }

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

    private fun formatMethodName(methodName: String): String {
        return if (methodName.contains(".")) {
            methodName.substring(methodName.lastIndexOf(".") + 1)
        } else {
            methodName
        }
    }

    /**
     * Formats the name of the field that is affected so that
     * all fields have the form "this.nameOfField".
     *
     * @param name the name of the field.
     * @return the formatted version of the name.
     */
    private fun formatClassFieldName(name: String): String {
        return if (name.startsWith("this.")) {
            name
        } else {
            "this.$name"
        }
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
