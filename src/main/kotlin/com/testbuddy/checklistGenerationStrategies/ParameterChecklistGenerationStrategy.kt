package com.testbuddy.com.testbuddy.checklistGenerationStrategies

import com.intellij.psi.PsiParameter
import com.testbuddy.com.testbuddy.models.TestingChecklistLeaf

class ParameterChecklistGenerationStrategy : ChecklistGenerator<PsiParameter, TestingChecklistLeaf> {

    companion object Factory {
        fun create(): ParameterChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiParameter): List<TestingChecklistLeaf> {
        TODO("Not yet implemented")
    }
}
