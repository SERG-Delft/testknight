package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiMethod
import com.testbuddy.com.testbuddy.models.TestingChecklistItem

class MethodChecklistGenerationStrategy : ChecklistGenerator<PsiMethod> {

    companion object Factory {
        fun create(): MethodChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiMethod): List<TestingChecklistItem> {
        TODO("Not yet implemented")
    }
}
