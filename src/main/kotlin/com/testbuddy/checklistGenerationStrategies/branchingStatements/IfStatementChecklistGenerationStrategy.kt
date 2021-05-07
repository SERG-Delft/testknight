package com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements

import com.intellij.psi.PsiIfStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGenerator
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class IfStatementChecklistGenerationStrategy :
    ChecklistGenerator<PsiIfStatement> {

    companion object Factory {
        fun create(): IfStatementChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiIfStatement): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
