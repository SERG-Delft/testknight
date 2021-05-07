package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiParameter
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class ParameterChecklistGenerator : ChecklistGenerator<PsiParameter> {

    companion object Factory {
        fun create(): ParameterChecklistGenerator {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiParameter): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
