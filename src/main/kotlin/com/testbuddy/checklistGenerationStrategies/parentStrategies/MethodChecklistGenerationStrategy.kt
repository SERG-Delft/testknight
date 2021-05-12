package com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiMethod
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.com.testbuddy.models.TestingChecklistMethodNode

class MethodChecklistGenerationStrategy private constructor() :
    ParentChecklistGeneratorStrategy<PsiMethod, TestingChecklistMethodNode> {

    companion object Factory {
        fun create(): MethodChecklistGenerationStrategy {
            return MethodChecklistGenerationStrategy()
        }
    }

    override fun generateChecklist(psiMethod: PsiMethod): TestingChecklistMethodNode {
        val name = psiMethod.name
        val children = mutableListOf<TestingChecklistLeafNode>()
        return TestingChecklistMethodNode(name, children, psiMethod)
    }
}
