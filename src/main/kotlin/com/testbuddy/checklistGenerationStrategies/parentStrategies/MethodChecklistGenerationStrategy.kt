package com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.com.testbuddy.models.TestingChecklistMethodNode

class MethodChecklistGenerationStrategy private constructor(
    val nestedStructureTypesRecognized: List<Class<out PsiElement>>
) :
    ParentChecklistGeneratorStrategy<PsiMethod, TestingChecklistMethodNode> {

    companion object Factory {

        fun create(): MethodChecklistGenerationStrategy {
            val recognizedStructs = getDefaultStructureTypesRecognized()
            return create(recognizedStructs)
        }

        fun create(nestedStructureTypesRecognized: List<Class<out PsiElement>>): MethodChecklistGenerationStrategy {
            return MethodChecklistGenerationStrategy(nestedStructureTypesRecognized)
        }

        /**
         * Returns a list of the types supported.
         * For now those types are hardcoded but in the future we will
         * probably load them from a file (that is why this is an entire method instead of a field)
         */
        private fun getDefaultStructureTypesRecognized(): List<Class<out PsiElement>> {
            return listOf(PsiIfStatement::class.java, PsiSwitchStatement::class.java, PsiTryStatement::class.java,
                PsiParameter::class.java, PsiWhileStatement::class.java, PsiForStatement::class.java, PsiDoWhileStatement::class.java,
                PsiForeachStatement::class.java, PsiThrowStatement::class.java)
        }
    }

    override fun generateChecklist(psiMethod: PsiMethod): TestingChecklistMethodNode {
        val name = psiMethod.name
        val children = mutableListOf<TestingChecklistLeafNode>()
        val list = listOf(PsiMethod::class.java, PsiClass::class.java)
        return TestingChecklistMethodNode(name, children, psiMethod)
    }
}
