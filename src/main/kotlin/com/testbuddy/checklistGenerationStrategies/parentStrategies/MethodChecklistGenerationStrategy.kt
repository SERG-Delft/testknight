package com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiConditionalExpression
import com.intellij.psi.PsiDoWhileStatement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiForeachStatement
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiParameterList
import com.intellij.psi.PsiSwitchStatement
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.PsiTryStatement
import com.intellij.psi.PsiWhileStatement
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.models.TestingChecklistMethodNode
import com.testbuddy.utilities.ChecklistLeafNodeGenerator

class MethodChecklistGenerationStrategy private constructor(
    private val nestedStructureTypesRecognized: Array<Class<out PsiElement>>,
    private val checklistLeafNodeGenerator: ChecklistLeafNodeGenerator
) :
    ParentChecklistGeneratorStrategy<PsiMethod, TestingChecklistMethodNode> {

    companion object Factory {
        /**
         * Creates a new MethodChecklistGenerationStrategy.
         *
         * @return a MethodChecklistGenerationStrategy object with default settings.
         */
        fun create(): MethodChecklistGenerationStrategy {
            val recognizedStructs = getDefaultStructureTypesRecognized()
            val generationService = ChecklistLeafNodeGenerator()
            return create(recognizedStructs, generationService)
        }

        /**
         * Creates a new MethodChecklistGenerationStrategy.
         *
         * @param nestedStructureTypesRecognized the types of nested structures.
         * @param generator the ChecklistLeafGenerator to be used to generate the elements.
         * @return a MethodChecklistGenerationStrategy object.
         */
        fun create(
            nestedStructureTypesRecognized: Array<Class<out PsiElement>>,
            generator: ChecklistLeafNodeGenerator
        ): MethodChecklistGenerationStrategy {
            return MethodChecklistGenerationStrategy(nestedStructureTypesRecognized, generator)
        }

        /**
         * Creates a new MethodChecklistGenerationStrategy.
         *
         * @param generator the ChecklistLeafGenerator to be used to generate the elements.
         * @return a MethodChecklistGenerationStrategy object.
         */
        fun create(generator: ChecklistLeafNodeGenerator): MethodChecklistGenerationStrategy {
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
                PsiTryStatement::class.java, PsiParameterList::class.java,
                PsiWhileStatement::class.java, PsiForStatement::class.java,
                PsiDoWhileStatement::class.java, PsiForeachStatement::class.java,
                PsiThrowStatement::class.java, PsiConditionalExpression::class.java
            )
        }
    }

    /**
     * Generates the checklist for a given method.
     *
     * @param psiElement the method to generate on.
     * @return a TestingChecklistMethodNode representing the checklist for the method.
     */
    override fun generateChecklist(psiElement: PsiMethod): TestingChecklistMethodNode {
        val name = psiElement.name
        val children = mutableListOf<TestingChecklistLeafNode>()

        @Suppress("SpreadOperator")
        val nestedStructures = PsiTreeUtil.findChildrenOfAnyType(psiElement, *this.nestedStructureTypesRecognized)

        nestedStructures.forEach {
            children.addAll(
                checklistLeafNodeGenerator.generateChecklist(it) as Collection<TestingChecklistLeafNode>
            )
        }

        return TestingChecklistMethodNode(name, children, psiElement)
    }
}
