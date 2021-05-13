package com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements

import com.intellij.psi.PsiDoWhileStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class DoWhileStatementChecklistGenerationStrategy :
    LeafChecklistGeneratorStrategy<PsiDoWhileStatement> {

    companion object Factory {
        fun create(): DoWhileStatementChecklistGenerationStrategy {
            return DoWhileStatementChecklistGenerationStrategy()
        }
    }

    override fun generateChecklist(psiElement: PsiDoWhileStatement): List<TestingChecklistLeafNode> {
        return emptyList()
    }
}
