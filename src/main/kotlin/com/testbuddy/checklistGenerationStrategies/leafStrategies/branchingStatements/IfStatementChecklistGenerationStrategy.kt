package com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements

import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiLiteralExpression
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.BinaryExpressionChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class IfStatementChecklistGenerationStrategy private constructor(
    private val conditionChecklistGenerator: BinaryExpressionChecklistGenerationStrategy
):
    LeafChecklistGeneratorStrategy<PsiIfStatement> {

    companion object Factory {
        fun create(): IfStatementChecklistGenerationStrategy {
            val conditionChecklistGenerator = BinaryExpressionChecklistGenerationStrategy.create()
            return create(conditionChecklistGenerator)
        }

        fun create(conditionChecklistGenerator: BinaryExpressionChecklistGenerationStrategy):
                IfStatementChecklistGenerationStrategy {
            return IfStatementChecklistGenerationStrategy(conditionChecklistGenerator)
        }
    }

    override fun generateChecklist(psiElement: PsiIfStatement): List<TestingChecklistLeafNode> {
        val condition = psiElement.condition
        if (condition == null || condition is PsiLiteralExpression) {
            return emptyList()
        }
        return conditionChecklistGenerator.generateChecklist(condition)
    }
}
