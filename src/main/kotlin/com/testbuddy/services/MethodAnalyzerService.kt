package com.testbuddy.services

import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiDeclarationStatement
import com.intellij.psi.PsiJavaCodeReferenceElement
import com.intellij.psi.PsiLocalVariable
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiThisExpression
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.models.MutatesClassFieldSideEffect
import com.testbuddy.com.testbuddy.models.SideEffect
import com.testbuddy.com.testbuddy.models.ThrowsExceptionSideEffect

class MethodAnalyzerService {

    /**
     * Gets the side effects of a given method. It currently
     * recognises exceptions and class field mutations as side-effects.
     *
     * @param method the PsiMethod to be analyzed.
     * @return a list of SideEffect objects based on the side-effects the method has.
     */
    fun getSideEffects(method: PsiMethod): List<SideEffect> {
        val classFieldMutationSideEffects = getClassFieldsAffected(method)
        return classFieldMutationSideEffects
    }

    /**
     * Finds all the class fields affected by the method.
     *
     * @param method the method to be analyzed.
     * @return a list of MutatesClassFieldSideEffect objects representing the class fields that change.
     */
    private fun getClassFieldsAffected(method: PsiMethod): List<MutatesClassFieldSideEffect> {
        val identifiersInMethodScope = getIdentifiersInMethodScope(method)
        val assignmentExpressions = PsiTreeUtil.findChildrenOfType(method, PsiAssignmentExpression::class.java)
        val assignmentsThatAffectClassFields =
            assignmentExpressions.filter { affectsClassField(it, identifiersInMethodScope) }
        return assignmentsThatAffectClassFields.map {
            MutatesClassFieldSideEffect(
                formatClassFieldName((it.lExpression as PsiReferenceExpression).qualifiedName)
            )
        }
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
        identifiersInMethodScope: Set<String>
    ): Boolean {
        val leftExpression = assignment.lExpression
        if (leftExpression.firstChild is PsiThisExpression) {
            return true
        }
        val nameAffected = (leftExpression as PsiReferenceExpression).qualifiedName
        // if the expression is not in identifiersInMethodScope then it is a side effect
        return !identifiersInMethodScope.contains(nameAffected)
    }

    /**
     * Formats the name of the field that is affected so that
     * all fields have the form "this.nameOfField".
     *
     * @param name the name of the field.
     * @return the formatted version of the name.
     */
    private fun formatClassFieldName(name: String): String {
        return if (name.startsWith("this")) {
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
