package com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements

import com.intellij.psi.PsiTryStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class TryStatementChecklistGenerationStrategy :
    LeafChecklistGeneratorStrategy<PsiTryStatement> {

    companion object Factory {
        fun create(): TryStatementChecklistGenerationStrategy {
            return TryStatementChecklistGenerationStrategy()
        }
    }

    override fun generateChecklist(psiElement: PsiTryStatement): List<TestingChecklistLeafNode> {
        return emptyList()
    }
}
