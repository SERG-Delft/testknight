package com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements

import com.intellij.psi.PsiForStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.ConditionChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class ForStatementChecklistGenerationStrategy private constructor(
    private val conditionChecklistGenerator: ConditionChecklistGenerationStrategy
) :
    LeafChecklistGeneratorStrategy<PsiForStatement> {

    companion object Factory {

        fun create(): ForStatementChecklistGenerationStrategy {
            val conditionChecklistGenerator = ConditionChecklistGenerationStrategy.create()
            return create(conditionChecklistGenerator)
        }

        fun create(conditionChecklistGenerator: ConditionChecklistGenerationStrategy):
            ForStatementChecklistGenerationStrategy {
                return ForStatementChecklistGenerationStrategy(conditionChecklistGenerator)
            }
    }

    override fun generateChecklist(psiElement: PsiForStatement): List<TestingChecklistLeafNode> {

        val condition = psiElement.condition!!
        val mcdcChecklist = conditionChecklistGenerator.generateChecklist(condition)
        return mcdcChecklist +
            listOf(TestingChecklistLeafNode("Test where for loop runs multiple times", psiElement))
    }
}
