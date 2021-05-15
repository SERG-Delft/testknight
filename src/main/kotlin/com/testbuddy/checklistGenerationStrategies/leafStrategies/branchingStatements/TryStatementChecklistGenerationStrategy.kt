package com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiCatchSection
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.models.TestingChecklistLeafNode

class TryStatementChecklistGenerationStrategy private constructor() :
    LeafChecklistGeneratorStrategy<PsiTryStatement> {

    companion object Factory {
        /**
         * Creates a new TryStatementChecklistGenerationStrategy.
         *
         * @return a TryStatementChecklistGenerationStrategy object.
         */
        fun create(): TryStatementChecklistGenerationStrategy {
            return TryStatementChecklistGenerationStrategy()
        }
    }

    /**
     * Generates the checklist for a given try statement.
     *
     * @param psiElement the try statement.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    override fun generateChecklist(psiElement: PsiTryStatement): List<TestingChecklistLeafNode> {
        val result = mutableListOf<TestingChecklistLeafNode>(
            TestingChecklistLeafNode(
                "Test with the try block running successfully",
                psiElement
            )
        )
        val catches = PsiTreeUtil.findChildrenOfType(psiElement, PsiCatchSection::class.java)
        if (catches.isEmpty()) {
            result.add(TestingChecklistLeafNode("Test with the try block throwing an exception", psiElement))
            return result
        }
        catches.forEach {
            if (it.catchType != null) result.add(
                TestingChecklistLeafNode(
                    "Test with the try block throwing a ${it.catchType!!.canonicalText}",
                    it
                )
            )
        }
        return result
    }
}
