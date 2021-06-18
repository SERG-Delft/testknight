package com.testknight.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiConditionalExpression
import com.intellij.psi.PsiLiteralExpression
import com.testknight.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testknight.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testknight.models.testingChecklist.leafNodes.ConditionChecklistNode
import com.testknight.settings.SettingsService

class ConditionalExpressionChecklistGenerationStrategy private constructor(
    private val conditionChecklistGenerator: ConditionChecklistGenerationStrategy
) :
    LeafChecklistGeneratorStrategy<PsiConditionalExpression> {

    companion object Factory {

        @Suppress("UnusedPrivateMember")
        private const val defaultConditionCoverageType = "MC/DC"

        /**
         * Creates a new ConditionalExpressionChecklistGenerationStrategy.
         *
         * @return a new ConditionalExpressionChecklistGenerationStrategy.
         */
        fun create(): ConditionalExpressionChecklistGenerationStrategy {
            val conditionChecklistGenerator =
                ConditionChecklistGenerationStrategy
                    .createFromString(
                        getConditionCoverageType()
                    )
            return create(conditionChecklistGenerator)
        }

        /**
         * Creates a new ConditionalExpressionChecklistGenerationStrategy.
         *
         * @param conditionChecklistGenerator the MC/DC checklist generator to use on the ternary operator's condition.
         * @return a new ConditionalExpressionChecklistGenerationStrategy.
         */
        fun create(conditionChecklistGenerator: ConditionChecklistGenerationStrategy):
            ConditionalExpressionChecklistGenerationStrategy {
                return ConditionalExpressionChecklistGenerationStrategy(conditionChecklistGenerator)
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
     * Generates the checklist for a given ternary operator.
     *
     * @param psiElement the conditional expression for which the checklist is to be generated.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    override fun generateChecklist(psiElement: PsiConditionalExpression): List<ConditionChecklistNode> {

        val condition = psiElement.condition

        if (condition is PsiLiteralExpression) {
            return emptyList()
        }
        return conditionChecklistGenerator.generateChecklist(condition)
    }
}
