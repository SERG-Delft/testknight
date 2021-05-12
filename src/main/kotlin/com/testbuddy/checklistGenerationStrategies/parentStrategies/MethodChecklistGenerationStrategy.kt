package com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiMethod
import com.testbuddy.com.testbuddy.models.TestingChecklistMethodNode

class MethodChecklistGenerationStrategy :
    ParentChecklistGeneratorStrategy<PsiMethod, TestingChecklistMethodNode> {

    companion object Factory {
        fun create(): MethodChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiMethod): TestingChecklistMethodNode {
        TODO("Not yet implemented")
    }
}
