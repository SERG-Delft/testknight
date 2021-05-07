package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiTryStatement
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class TryStatementChecklistGenerator : ChecklistGenerator<PsiTryStatement> {

    companion object Factory {
        fun create(): TryStatementChecklistGenerator {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiTryStatement): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
