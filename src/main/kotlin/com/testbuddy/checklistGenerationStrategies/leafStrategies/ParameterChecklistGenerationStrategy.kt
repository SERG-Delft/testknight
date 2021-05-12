package com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiParameter
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class ParameterChecklistGenerationStrategy :
    LeafChecklistGeneratorStrategy<PsiParameter> {

    companion object Factory {
        fun create(): ParameterChecklistGenerationStrategy {
            TODO("Not yet implemented")
        }
    }

    override fun generateChecklist(psiElement: PsiParameter): List<TestingChecklistLeafNode> {
        TODO("Not yet implemented")
    }
}
