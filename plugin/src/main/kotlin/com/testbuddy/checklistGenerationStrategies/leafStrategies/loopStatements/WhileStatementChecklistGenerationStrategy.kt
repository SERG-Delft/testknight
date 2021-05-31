package com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiWhileStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.messageBundleHandlers.TestingChecklistMessageBundleHandler
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.loopStatements.WhileStatementChecklistNode
import com.testbuddy.com.testbuddy.settings.SettingsService

class WhileStatementChecklistGenerationStrategy private constructor(
    private val conditionChecklistGenerator: ConditionChecklistGenerationStrategy
) :
    LeafChecklistGeneratorStrategy<PsiWhileStatement> {

    companion object Factory {

        /**
         * Creates a new WhileStatementChecklistGenerationStrategy.
         *
         * @return a new WhileStatementChecklistGenerationStrategy.
         */
        fun create(): WhileStatementChecklistGenerationStrategy {
            val settings = SettingsService.instance.state
            val conditionStrategy = ConditionChecklistGenerationStrategy
                .createFromString(settings.checklistSettings.coverageCriteria)

            return create(conditionStrategy)
        }

        /**
         * Creates a new WhileStatementChecklistGenerationStrategy.
         *
         * @param conditionChecklistGenerator the ConditionChecklistGenerator to use on the while loop's condition.
         * @return a new WhileStatementChecklistGenerationStrategy.
         */
        fun create(conditionChecklistGenerator: ConditionChecklistGenerationStrategy):
            WhileStatementChecklistGenerationStrategy {
                return WhileStatementChecklistGenerationStrategy(conditionChecklistGenerator)
            }
    }

    /**
     * Generates the checklist for a given while loop.
     *
     * @param psiElement the while loop PSI element for which the checklist is to be generated.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    override fun generateChecklist(psiElement: PsiWhileStatement): List<TestingChecklistLeafNode> {

        val condition = psiElement.condition ?: return emptyList()
        val mcdcChecklist = conditionChecklistGenerator.generateChecklist(condition)
        return mcdcChecklist +
            listOf(
                WhileStatementChecklistNode(
                    TestingChecklistMessageBundleHandler.message("whileLoopMultiple"),
                    psiElement
                )
            )
    }
}
