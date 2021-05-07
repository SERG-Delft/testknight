package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiSwitchStatement
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class SwitchStatementChecklistGenerator : ChecklistGenerator<PsiSwitchStatement> {

    companion object Factory {
        fun create(): SwitchStatementChecklistGenerator {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiSwitchStatement): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
