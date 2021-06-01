package com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testbuddy.settings.SettingsService
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

            val settingsService = SettingsService.instance
            val settings = settingsService.state
            val recognizedStructures = mutableListOf<Class<out PsiElement>>()

            // for each checklist strategy setting if enabled in settings add to recognized structures
            settings.checklistSettings.checklistStrategies.entries.forEach { (str, enabled) ->
                if (enabled) recognizedStructures.add(settingsService.strategyNames[str]!!)
            }

            return recognizedStructures.toTypedArray()
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
