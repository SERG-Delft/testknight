package com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiNewExpression
import com.intellij.psi.PsiThrowStatement
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

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
    @Suppress
    override fun generateChecklist(psiElement: PsiThrowStatement): List<TestingChecklistLeafNode> {
        val newExpression = (psiElement.exception as? PsiNewExpression)
        val nameOfException = newExpression?.classReference?.qualifiedName
        if (newExpression == null || nameOfException == null) return emptyList()
        return listOf(TestingChecklistLeafNode("Test when $nameOfException is thrown", psiElement))
    }
}
