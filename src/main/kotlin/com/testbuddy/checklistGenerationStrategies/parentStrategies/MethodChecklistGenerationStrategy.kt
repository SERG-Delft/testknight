package com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.openapi.components.ServiceManager
import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameter
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.PsiWhileStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.com.testbuddy.models.TestingChecklistMethodNode
import com.testbuddy.com.testbuddy.utilities.ChecklistLeafNodeGenerator
import com.testbuddy.services.GenerateTestCaseChecklistService

class MethodChecklistGenerationStrategy private constructor(
    private val nestedStructureTypesRecognized: Array<Class<out PsiElement>>,
    private val checklistLeafNodeGenerator: ChecklistLeafNodeGenerator
) :
    ParentChecklistGeneratorStrategy<PsiMethod, TestingChecklistMethodNode> {


    companion object Factory {

        fun create(): MethodChecklistGenerationStrategy {
            val recognizedStructs = getDefaultStructureTypesRecognized()
            val generationService = ChecklistLeafNodeGenerator()
            return create(recognizedStructs, generationService)
        }

        fun create(
            nestedStructureTypesRecognized: Array<Class<out PsiElement>>,
            generator: ChecklistLeafNodeGenerator
        ): MethodChecklistGenerationStrategy {
            return MethodChecklistGenerationStrategy(nestedStructureTypesRecognized, generator);
        }

        fun create(generator: ChecklistLeafNodeGenerator) : MethodChecklistGenerationStrategy {
            val recognizedStructs = getDefaultStructureTypesRecognized()
            return MethodChecklistGenerationStrategy(recognizedStructs, generator)
        }

        /**
         * Returns a list of the types supported.
         * For now those types are hardcoded but in the future we will
         * probably load them from a file (that is why this is an entire method instead of a field)
         */
        private fun getDefaultStructureTypesRecognized(): Array<Class<out PsiElement>> {
            return arrayOf(
                PsiIfStatement::class.java, PsiSwitchStatement::class.java,
                PsiTryStatement::class.java, PsiParameter::class.java,
                PsiWhileStatement::class.java, PsiForStatement::class.java,
                PsiDoWhileStatement::class.java, PsiForeachStatement::class.java,
                PsiThrowStatement::class.java
            )
        }
    }

    override fun generateChecklist(psiMethod: PsiMethod): TestingChecklistMethodNode {
        val name = psiMethod.name
        val children = mutableListOf<TestingChecklistLeafNode>()

        @Suppress("SpreadOperator")
        val nestedStructures = PsiTreeUtil.findChildrenOfAnyType(psiMethod, *this.nestedStructureTypesRecognized)

        nestedStructures.forEach {
            children.addAll(
                checklistLeafNodeGenerator.generateChecklist(it) as Collection<TestingChecklistLeafNode>
            )
        }

        return TestingChecklistMethodNode(name, children, psiMethod)
    }
}
