package com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements

import com.intellij.psi.PsiForStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class ForStatementChecklistGenerationStrategy :
    LeafChecklistGeneratorStrategy<PsiForStatement> {

    companion object Factory {
        fun create(): ForStatementChecklistGenerationStrategy {
            return ForStatementChecklistGenerationStrategy()
        }
    }

    override fun generateChecklist(psiElement: PsiForStatement): List<TestingChecklistLeafNode> {
        TODO("Not yet implemented")
    }
}
