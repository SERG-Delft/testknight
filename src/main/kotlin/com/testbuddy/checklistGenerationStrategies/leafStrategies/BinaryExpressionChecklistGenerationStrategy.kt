package com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiBinaryExpression
import com.testbuddy.com.testbuddy.models.TestingChecklistLeaf

class BinaryExpressionChecklistGenerationStrategy :
    LeafChecklistGeneratorStrategy<PsiBinaryExpression> {

    companion object Factory {
        fun create(): BinaryExpressionChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiBinaryExpression): List<TestingChecklistLeaf> {
        /**
         * if the operand is numeric -> base case
         * if the operand is logical -> recurse left and right
         */
        TODO("Not yet implemented")
    }
}
