package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiClass
import com.testbuddy.com.testbuddy.models.TestingChecklistClassNode

class ClassChecklistGenerationStrategy : ChecklistGenerator<PsiClass, TestingChecklistClassNode> {

    companion object Factory {
        fun create(): ClassChecklistGenerationStrategy {
            TODO()
        }
    }

    override fun generateChecklist(psiElement: PsiClass): List<TestingChecklistClassNode> {
        TODO("Not yet implemented")
    }
}
