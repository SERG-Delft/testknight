package com.testknight.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiForStatement
import com.testknight.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testknight.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testknight.messageBundleHandlers.TestingChecklistMessageBundleHandler
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testknight.models.testingChecklist.leafNodes.loopStatements.ForLoopStatementChecklistNode
import com.testknight.settings.SettingsService

class ForStatementChecklistGenerationStrategy private constructor(
    private val conditionChecklistGenerator: ConditionChecklistGenerationStrategy
) :
    LeafChecklistGeneratorStrategy<PsiForStatement> {

    companion object Factory {

        /**
         * Creates a new ForStatementChecklistGenerationStrategy.
         *
         * @return a new ForStatementChecklistGenerationStrategy.
         */
        fun create(): ForStatementChecklistGenerationStrategy {
            val settings = SettingsService.instance.state
            val conditionStrategy = ConditionChecklistGenerationStrategy
                .createFromString(settings.checklistSettings.coverageCriteria)

            return create(conditionStrategy)
        }

        /**
         * Creates a new ForStatementChecklistGenerationStrategy.
         *
         * @param conditionChecklistGenerator the ConditionChecklistGenerator to use on the for loop's condition .
         * @return a new ForStatementChecklistGenerationStrategy.
         */
        fun create(conditionChecklistGenerator: ConditionChecklistGenerationStrategy):
            ForStatementChecklistGenerationStrategy {
                return ForStatementChecklistGenerationStrategy(conditionChecklistGenerator)
            }
    }

    /**
     * Generates the checklist for a given for loop.
     *
     * @param psiElement the for loop PSI element for which the checklist is to be generated.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    override fun generateChecklist(psiElement: PsiForStatement): List<TestingChecklistLeafNode> {

        val condition = psiElement.condition ?: return emptyList()
        val mcdcChecklist = conditionChecklistGenerator.generateChecklist(condition)
        return mcdcChecklist +
            listOf(
                ForLoopStatementChecklistNode(
                    TestingChecklistMessageBundleHandler.message("forLoopMultiple"),
                    psiElement
                )
            )
    }
}
