package com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements

import com.intellij.psi.PsiIfStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class IfStatementChecklistGenerationStrategy :
    LeafChecklistGeneratorStrategy<PsiIfStatement> {

    companion object Factory {
        fun create(): IfStatementChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiIfStatement): List<TestingChecklistLeafNode> {
        TODO("Not yet implemented")
    }
}
