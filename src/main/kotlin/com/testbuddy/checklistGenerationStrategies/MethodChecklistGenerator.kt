package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiMethod
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class MethodChecklistGenerator : ChecklistGenerator<PsiMethod> {

    companion object Factory {
        fun create(): MethodChecklistGenerator {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiMethod): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
