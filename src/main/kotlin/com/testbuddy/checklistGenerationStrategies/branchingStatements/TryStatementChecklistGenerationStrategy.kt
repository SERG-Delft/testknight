package com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements

import com.intellij.psi.PsiTryStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGenerator
import com.testbuddy.com.testbuddy.models.TestingChecklistLeaf

class TryStatementChecklistGenerationStrategy :
    ChecklistGenerator<PsiTryStatement, TestingChecklistLeaf> {

    companion object Factory {
        fun create(): TryStatementChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiTryStatement): List<TestingChecklistLeaf> {
        TODO("Not yet implemented")
    }
}
