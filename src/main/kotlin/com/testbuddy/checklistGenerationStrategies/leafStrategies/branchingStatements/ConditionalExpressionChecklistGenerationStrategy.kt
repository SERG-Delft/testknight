package com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiConditionalExpression
import com.intellij.psi.PsiLiteralExpression
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class ConditionalExpressionChecklistGenerationStrategy private constructor(
    private val conditionChecklistGenerator: ConditionChecklistGenerationStrategy
) :
    LeafChecklistGeneratorStrategy<PsiConditionalExpression> {

    companion object Factory {

        /**
         * Creates a new ConditionalExpressionChecklistGenerationStrategy.
         *
         * @return a new ConditionalExpressionChecklistGenerationStrategy.
         */
        fun create(): ConditionalExpressionChecklistGenerationStrategy {
            val conditionChecklistGenerator = ConditionChecklistGenerationStrategy.create()
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
    }

    /**
     * Generates the checklist for a given ternary operator.
     *
     * @param psiElement the conditional expression for which the checklist is to be generated.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    override fun generateChecklist(psiElement: PsiConditionalExpression): List<TestingChecklistLeafNode> {

        val condition = psiElement.condition

        if (condition == null || condition is PsiLiteralExpression) {
            return emptyList()
        }
        return conditionChecklistGenerator.generateChecklist(condition)
    }
}
