package com.testbuddy.com.testbuddy.checklistGenerationStrategies.branchingStatements

import com.intellij.psi.PsiSwitchStatement
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGenerator
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class SwitchStatementChecklistGenerationStrategy :
    ChecklistGenerator<PsiSwitchStatement> {

    companion object Factory {
        fun create(): SwitchStatementChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiSwitchStatement): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
