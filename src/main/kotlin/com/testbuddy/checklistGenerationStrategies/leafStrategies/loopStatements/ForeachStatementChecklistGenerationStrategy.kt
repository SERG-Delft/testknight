package com.testbuddy.checklistGenerationStrategies.leafStrategies.loopStatements

import com.intellij.psi.PsiForeachStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class ForeachStatementChecklistGenerationStrategy private constructor() :
    LeafChecklistGeneratorStrategy<PsiForeachStatement> {

    companion object Factory {

        fun create(): ForeachStatementChecklistGenerationStrategy {
            return ForeachStatementChecklistGenerationStrategy()
        }
    }

    override fun generateChecklist(psiElement: PsiForeachStatement): List<TestingChecklistLeafNode> {

        val iteratedValue = psiElement.iteratedValue!!.text
        return listOf(
            TestingChecklistLeafNode(description = "Test where $iteratedValue empty", psiElement),
            TestingChecklistLeafNode(description = "Test where $iteratedValue null", psiElement),
            TestingChecklistLeafNode(description = "Test where foreach loop runs multiple times", psiElement)
        )
    }
}
