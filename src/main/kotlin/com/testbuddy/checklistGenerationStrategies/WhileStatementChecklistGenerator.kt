package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiWhileStatement
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class WhileStatementChecklistGenerator : ChecklistGenerator<PsiWhileStatement> {

    companion object Factory {

        fun create(): WhileStatementChecklistGenerator {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiWhileStatement): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
