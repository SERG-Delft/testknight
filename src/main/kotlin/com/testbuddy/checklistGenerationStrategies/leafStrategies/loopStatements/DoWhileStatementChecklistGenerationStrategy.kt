package com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiDoWhileStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class DoWhileStatementChecklistGenerationStrategy private constructor(
    private val conditionChecklistGenerator: ConditionChecklistGenerationStrategy
) :
    LeafChecklistGeneratorStrategy<PsiDoWhileStatement> {

    companion object Factory {
        fun create(): DoWhileStatementChecklistGenerationStrategy {
            val conditionChecklistGenerator = ConditionChecklistGenerationStrategy.create()
            return create(conditionChecklistGenerator)
        }

        fun create(conditionChecklistGenerator: ConditionChecklistGenerationStrategy):
            DoWhileStatementChecklistGenerationStrategy {
                return DoWhileStatementChecklistGenerationStrategy(conditionChecklistGenerator)
            }
    }

    override fun generateChecklist(psiElement: PsiDoWhileStatement): List<TestingChecklistLeafNode> {

        val condition = psiElement.condition!!
        val mcdcChecklist = conditionChecklistGenerator.generateChecklist(condition)
        return mcdcChecklist +
            listOf(TestingChecklistLeafNode("Test where do-while loop runs multiple times", psiElement))
    }
}
