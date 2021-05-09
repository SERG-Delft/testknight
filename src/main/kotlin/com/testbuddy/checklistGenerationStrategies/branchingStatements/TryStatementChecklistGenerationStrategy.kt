package com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements

import com.intellij.psi.PsiTryStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGenerator
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class TryStatementChecklistGenerationStrategy :
    ChecklistGenerator<PsiTryStatement> {

    companion object Factory {
        fun create(): TryStatementChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiTryStatement): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
