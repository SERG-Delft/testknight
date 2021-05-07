package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiForStatement
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class ForStatementChecklistGenerator : ChecklistGenerator<PsiForStatement> {

    companion object Factory {
        fun create(): ForStatementChecklistGenerator {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiForStatement): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
