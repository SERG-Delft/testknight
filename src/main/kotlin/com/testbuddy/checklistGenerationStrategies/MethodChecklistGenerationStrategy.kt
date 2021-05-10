package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiMethod
import com.testbuddy.com.testbuddy.models.TestingChecklistMethodNode

class MethodChecklistGenerationStrategy : ChecklistGenerator<PsiMethod, TestingChecklistMethodNode> {

    companion object Factory {
        fun create(): MethodChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiMethod): List<TestingChecklistMethodNode> {
        TODO("Not yet implemented")
    }
}
