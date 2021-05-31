package com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiLiteralExpression
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.exceptions.InvalidConfigurationException
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.ConditionChecklistNode
import com.testbuddy.com.testbuddy.settings.SettingsService

class IfStatementChecklistGenerationStrategy private constructor(
    private val conditionChecklistGenerator: ConditionChecklistGenerationStrategy
) :
    LeafChecklistGeneratorStrategy<PsiIfStatement> {

    companion object Factory {

        @Suppress("UnusedPrivateMember")
        private const val defaultConditionCoverageType = "MC/DC"

        /**
         * Creates a new IfStatementChecklistGenerationStrategy.
         *
         * @return a new IfStatementChecklistGenerationStrategy.
         */
        @Throws(InvalidConfigurationException::class)
        fun create(): IfStatementChecklistGenerationStrategy {
            val conditionChecklistGenerator =
                ConditionChecklistGenerationStrategy
                    .createFromString(getConditionCoverageType())
            return create(conditionChecklistGenerator)
        }

        /**
         * Creates a new IfStatementChecklistGenerationStrategy.
         *
         * @param conditionChecklistGenerator the ConditionChecklistGenerator to use on the if's condition.
         * @return a new IfStatementChecklistGenerationStrategy.
         */
        fun create(conditionChecklistGenerator: ConditionChecklistGenerationStrategy):
            IfStatementChecklistGenerationStrategy {
                return IfStatementChecklistGenerationStrategy(conditionChecklistGenerator)
            }

        /**
         * Returns the configured condition coverage type.
         *
         * @return a string representing the configured condition coverage.
         */
        private fun getConditionCoverageType(): String {
            val settings = SettingsService.instance.state
            return settings.checklistSettings.coverageCriteria
        }
    }

    /**
     * Generates the checklist for a given if statement.
     *
     * @param psiElement the if statement.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    override fun generateChecklist(psiElement: PsiIfStatement): List<ConditionChecklistNode> {
        val condition = psiElement.condition
        if (condition == null || condition is PsiLiteralExpression) {
            return emptyList()
        }
        return conditionChecklistGenerator.generateChecklist(condition)
    }
}
