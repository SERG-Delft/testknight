package com.testbuddy.com.testbuddy.checklistGenerationStrategies.leafStrategies

import com.intellij.psi.PsiParameter
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode

class ParameterChecklistGenerationStrategy :
    LeafChecklistGeneratorStrategy<PsiParameter> {

    companion object Factory {
        fun create(): ParameterChecklistGenerationStrategy {
            return ParameterChecklistGenerationStrategy()
        }
    }

    override fun generateChecklist(psiElement: PsiParameter): List<TestingChecklistLeafNode> {
        return emptyList()
    }
}
