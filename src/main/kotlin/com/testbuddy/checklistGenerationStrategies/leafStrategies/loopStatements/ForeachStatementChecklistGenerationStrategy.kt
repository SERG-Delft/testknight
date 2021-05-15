package com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiForeachStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class ForeachStatementChecklistGenerationStrategy private constructor() :
    LeafChecklistGeneratorStrategy<PsiForeachStatement> {

    companion object Factory {

        /**
         * Creates a new ForEachStatementChecklistGenerationStrategy.
         *
         * @return a new ForEachStatementChecklistGenerationStrategy.
         */
        fun create(): ForeachStatementChecklistGenerationStrategy {
            return ForeachStatementChecklistGenerationStrategy()
        }
    }

    /**
     * Generates the checklist for a given foreach loop.
     *
     * @param psiElement the foreach loop PSI element for which the checklist is to be generated.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    override fun generateChecklist(psiElement: PsiForeachStatement): List<TestingChecklistLeafNode> {

        if (psiElement.iteratedValue == null) {
            return emptyList()
        }
        val iteratedValue = psiElement.iteratedValue!!.text
        return listOf(
            TestingChecklistLeafNode(description = "Test where $iteratedValue is empty", psiElement),
            TestingChecklistLeafNode(description = "Test where $iteratedValue is null", psiElement),
            TestingChecklistLeafNode(description = "Test where foreach loop runs multiple times", psiElement)
        )
    }
}
