package com.testknight.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiNewExpression
import com.intellij.psi.PsiThrowStatement
import com.testknight.messageBundleHandlers.TestingChecklistMessageBundleHandler
import com.testknight.models.testingChecklist.leafNodes.ThrowStatementChecklistNode

class ThrowStatementChecklistGenerationStrategy private constructor() :
    LeafChecklistGeneratorStrategy<PsiThrowStatement> {

    companion object Factory {
        /**
         * Creates a new ThrowStatementChecklistGenerationStrategy object.
         *
         * @return a ThrowStatementChecklistGenerationStrategy object.
         */
        fun create(): ThrowStatementChecklistGenerationStrategy {
            return ThrowStatementChecklistGenerationStrategy()
        }
    }

    /**
     * Generates the checklist for a given throw statement.
     *
     * @param psiElement the throw statement.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */

    override fun generateChecklist(psiElement: PsiThrowStatement): List<ThrowStatementChecklistNode> {
        val newExpression = (psiElement.exception as? PsiNewExpression)
        val nameOfException = newExpression?.classReference?.qualifiedName
        if (newExpression == null || nameOfException == null) return emptyList()
        return listOf(
            ThrowStatementChecklistNode(
                TestingChecklistMessageBundleHandler.message("throw", nameOfException),
                psiElement,
                nameOfException
            )
        )
    }
}
