package com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiConditionalExpression
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class ConditionalExpressionChecklistGenerationStrategy private constructor(
    private val conditionChecklistGenerator: ConditionChecklistGenerationStrategy
) :
    LeafChecklistGeneratorStrategy<PsiConditionalExpression> {

    companion object Factory {

        fun create(): ConditionalExpressionChecklistGenerationStrategy {
            val conditionChecklistGenerator = ConditionChecklistGenerationStrategy.create()
            return create(conditionChecklistGenerator)
        }

        fun create(conditionChecklistGenerator: ConditionChecklistGenerationStrategy):
            ConditionalExpressionChecklistGenerationStrategy {
                return ConditionalExpressionChecklistGenerationStrategy(conditionChecklistGenerator)
            }
    }

    override fun generateChecklist(psiElement: PsiConditionalExpression): List<TestingChecklistLeafNode> {

        val condition = psiElement.condition!!
        return conditionChecklistGenerator.generateChecklist(condition)
    }
}
