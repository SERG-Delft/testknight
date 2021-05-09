package com.testbuddy.com.testbuddy.checklistGenerationStrategies.loopStatements

import com.intellij.psi.PsiWhileStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGenerator
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class WhileStatementChecklistGenerationStrategy :
    ChecklistGenerator<PsiWhileStatement> {

    companion object Factory {

        fun create(): WhileStatementChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiWhileStatement): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
