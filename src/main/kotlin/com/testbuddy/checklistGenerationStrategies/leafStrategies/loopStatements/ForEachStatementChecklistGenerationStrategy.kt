package com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiForeachStatement
import com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.messageBundleHandlers.TestingChecklistMessageBundleHandler
import com.testbuddy.models.TestingChecklistLeafNode

class ForEachStatementChecklistGenerationStrategy private constructor() :
    LeafChecklistGeneratorStrategy<PsiForeachStatement> {

    companion object Factory {

        /**
         * Creates a new ForEachStatementChecklistGenerationStrategy.
         *
         * @return a new ForEachStatementChecklistGenerationStrategy.
         */
        fun create(): ForEachStatementChecklistGenerationStrategy {
            return ForEachStatementChecklistGenerationStrategy()
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
            TestingChecklistLeafNode(
                description = TestingChecklistMessageBundleHandler.message(
                    "forEachEmpty",
                    iteratedValue
                ),
                psiElement
            ),
            TestingChecklistLeafNode(
                description = TestingChecklistMessageBundleHandler.message(
                    "forEachOnce",
                    iteratedValue
                ),
                psiElement
            ),
            TestingChecklistLeafNode(
                description = TestingChecklistMessageBundleHandler.message(
                    "forEachNull",
                    iteratedValue
                ),
                psiElement
            ),
            TestingChecklistLeafNode(
                description = TestingChecklistMessageBundleHandler
                    .message("forEachMultiple"),
                psiElement
            )
        )
    }
}
