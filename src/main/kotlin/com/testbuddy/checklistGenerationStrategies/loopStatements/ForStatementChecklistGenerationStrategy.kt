package com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements

import com.intellij.psi.PsiForStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGenerator
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class ForStatementChecklistGenerationStrategy :
    ChecklistGenerator<PsiForStatement> {

    companion object Factory {
        fun create(): ForStatementChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiForStatement): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
