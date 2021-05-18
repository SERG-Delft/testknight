package com.testbuddy.checklistGenerationStrategies.leafStrategies.branchingStatements

import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiLiteralExpression
import com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.models.TestingChecklistLeafNode

class IfStatementChecklistGenerationStrategy private constructor(
    private val conditionChecklistGenerator: ConditionChecklistGenerationStrategy
) :
    LeafChecklistGeneratorStrategy<PsiIfStatement> {

    companion object Factory {
        /**
         * Creates a new IfStatementChecklistGenerationStrategy.
         *
         * @return a new IfStatementChecklistGenerationStrategy.
         */
        fun create(): IfStatementChecklistGenerationStrategy {
            val conditionChecklistGenerator =
                ConditionChecklistGenerationStrategy
                    .createMcDcConditionCoverageGenerationStrategy()
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
    }

    /**
     * Generates the checklist for a given if statement.
     *
     * @param psiElement the if statement.
     * @return a list of TestingChecklistLeafNode objects corresponding to the required checklist items.
     */
    override fun generateChecklist(psiElement: PsiIfStatement): List<TestingChecklistLeafNode> {
        val condition = psiElement.condition
        if (condition == null || condition is PsiLiteralExpression) {
            return emptyList()
        }
        return conditionChecklistGenerator.generateChecklist(condition)
    }
}
