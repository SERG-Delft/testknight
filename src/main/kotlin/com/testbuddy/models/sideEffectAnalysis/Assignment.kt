package com.testbuddy.models.sideEffectAnalysis

import com.intellij.psi.PsiAssignmentExpression
import com.testbuddy.utilities.StringFormatter

data class Assignment(val affectedObjectName: String, val fieldAffected: String?) {

    companion object Factory {
        fun create(psiAssignmentExpression: PsiAssignmentExpression): Assignment {
            val nameAffected = psiAssignmentExpression.lExpression.text
            val formattedName = nameAffected.replace("this.", "")
            return if (formattedName.contains('.')) {
                val lastIndex = formattedName.lastIndexOf('.')
                val objectNameAffected = formattedName.substring(0, lastIndex)
                val fieldAffected = formattedName.substring(lastIndex + 1)
                Assignment(objectNameAffected, fieldAffected)
            } else {
                Assignment(nameAffected, null)
            }
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
        return !identifiersInMethodScope.contains(this.affectedObjectName) &&
            identifiersInClassScope.contains(this.affectedObjectName)
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
    ): List<ClassFieldMutationSideEffect> {
        return if (affectsClassField(identifiersInMethodScope, identifiersInClassScope)) {
            if (this.fieldAffected == null) {
                listOf(
                    ReassignsClassFieldSideEffect(
                        StringFormatter.formatClassFieldName(
                            this.affectedObjectName
                        )
                    )
                )
            } else {
                listOf(
                    ReassignmentOfTransitiveField(
                        StringFormatter.formatClassFieldName(this.affectedObjectName),
                        this.fieldAffected
                    )
                )
            }
        } else {
            emptyList()
        }
    }
}
