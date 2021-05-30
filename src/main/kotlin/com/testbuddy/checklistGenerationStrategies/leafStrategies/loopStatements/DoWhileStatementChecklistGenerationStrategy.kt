package com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiDoWhileStatement
import com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.messageBundleHandlers.TestingChecklistMessageBundleHandler
import com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.settings.SettingsService

class DoWhileStatementChecklistGenerationStrategy private constructor(
    private val conditionChecklistGenerator: ConditionChecklistGenerationStrategy
) :
    LeafChecklistGeneratorStrategy<PsiDoWhileStatement> {

    companion object Factory {

        /**
         * Creates a new DoWhileStatementChecklistGenerationStrategy.
         *
         * @return a new DoWhileStatementChecklistGenerationStrategy.
         */
        fun create(): DoWhileStatementChecklistGenerationStrategy {
            val settings = SettingsService.instance.state
            val conditionStrategy = ConditionChecklistGenerationStrategy
                .createFromString(settings.checklistSettings.coverageCriteria)

            return create(conditionStrategy)
        }

        /**
         * Creates a new DoWhileStatementChecklistGenerationStrategy.
         *
         * @param conditionChecklistGenerator the ConditionChecklistGenerator to use on the do-while loop's condition .
         * @return a new DoWhileStatementChecklistGenerationStrategy.
         */
        fun create(conditionChecklistGenerator: ConditionChecklistGenerationStrategy):
            DoWhileStatementChecklistGenerationStrategy {
                return DoWhileStatementChecklistGenerationStrategy(conditionChecklistGenerator)
            }
    }

    /**
     * Generates the checklist for a given do-while loop.
     *
     * @param psiElement the do-while loop PSI element for which the checklist is to be generated.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    override fun generateChecklist(psiElement: PsiDoWhileStatement): List<TestingChecklistLeafNode> {

        val condition = psiElement.condition ?: return emptyList()
        val mcdcChecklist = conditionChecklistGenerator.generateChecklist(condition)
        return mcdcChecklist +
            listOf(
                TestingChecklistLeafNode(
                    TestingChecklistMessageBundleHandler
                        .message("doWhileMultipleTimes"),
                    psiElement
                )
            )
    }
}
