package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiDoWhileStatement
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class DoWhileStatementChecklistGenerator : ChecklistGenerator<PsiDoWhileStatement> {

    companion object Factory {
        fun create(): DoWhileStatementChecklistGenerator {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiDoWhileStatement): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
