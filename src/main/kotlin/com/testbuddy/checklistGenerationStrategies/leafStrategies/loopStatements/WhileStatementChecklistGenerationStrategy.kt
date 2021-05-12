package com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements

import com.intellij.psi.PsiWhileStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies.LeafChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class WhileStatementChecklistGenerationStrategy :
    LeafChecklistGeneratorStrategy<PsiWhileStatement> {

    companion object Factory {

        fun create(): WhileStatementChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiWhileStatement): List<TestingChecklistLeafNode> {
        TODO("Not yet implemented")
    }
}
