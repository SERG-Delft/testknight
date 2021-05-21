package com.testbuddy.models.sideEffectAnalysis

import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiReferenceExpression
import com.testbuddy.utilities.StringFormatter

data class Assignment(val nameAffected: String) {

    companion object Factory {
        fun create(psiAssignmentExpression: PsiAssignmentExpression): Assignment {
            val leftExpression = psiAssignmentExpression.lExpression
            val nameAffected = (leftExpression as PsiReferenceExpression).qualifiedName
            return Assignment(nameAffected)
        }
    }

    /**
     * Checks whether the given assignment expression affects one of the class fields.
     *
     * @param identifiersInMethodScope the identifiers that this method binds.
     *                        This is passed as an argument for optimisation purposes.
     * @param identifiersInClassScope the identifiers in the class scope.
     * @return true iff the assignment affects a class field.
     */
    private fun affectsClassField(
        identifiersInMethodScope: Map<String, String>,
        identifiersInClassScope: Map<String, String>
    ): Boolean {
        return !identifiersInMethodScope.contains(this.nameAffected) &&
                identifiersInClassScope.contains(this.nameAffected)
    }

    /**
     * Returns all the class fields re-assigned by an assignment.
     *
     * @param identifiersInMethodScope the identifiers in the method scope.
     * @param identifiersInClassScope the identifiers in the method scope.
     * @return a list of ReassignsClassFieldSideEffect objects representing the reassignments.
     */
    fun getClassFieldsReassigned(
        identifiersInMethodScope: Map<String, String>,
        identifiersInClassScope: Map<String, String>
    ): List<ReassignsClassFieldSideEffect> {
        return if (affectsClassField(identifiersInMethodScope, identifiersInClassScope)) {
            listOf(
                ReassignsClassFieldSideEffect(
                    StringFormatter.formatClassFieldName(
                        this.nameAffected
                    )
                )
            )
        } else {
            emptyList()
        }
    }
}
