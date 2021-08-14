package com.testknight.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistMethodNode

class ClassChecklistGenerationStrategy private constructor(
    private val methodChecklistGenerator: ParentChecklistGeneratorStrategy<PsiMethod, TestingChecklistMethodNode>
) :
    ParentChecklistGeneratorStrategy<PsiClass, TestingChecklistClassNode> {

    companion object Factory {
        /**
         * Creates a new ClassChecklistGenerationStrategy.
         *
         * @param methodChecklistGenerator the generator to be used to generate the method classes.
         * Used mainly to allow dependency injection for testing.
         * @return a ClassChecklistGenerationStrategy object.
         */
        fun create(
            methodChecklistGenerator: ParentChecklistGeneratorStrategy<PsiMethod, TestingChecklistMethodNode>
        ): ClassChecklistGenerationStrategy {
            return ClassChecklistGenerationStrategy(methodChecklistGenerator)
        }

        /**
         * Creates a new ClassChecklistGenerationStrategy.
         *
         * @return a ClassChecklistGenerationStrategy object.
         */
        fun create(): ClassChecklistGenerationStrategy {
            return create(MethodChecklistGenerationStrategy.create())
        }
    }

    /**
     * Generates the checklist for a given class.
     *
     * @param psiElement the PsiClass to generate the checklist on.
     * @return a TestingChecklistClassNode object representing the checklist for that class.
     */
    override fun generateChecklist(psiElement: PsiClass): TestingChecklistClassNode {
        val methodName = psiElement.name ?: "No class name found"
        val methods = PsiTreeUtil.findChildrenOfType(psiElement, PsiMethod::class.java)
        val children = mutableListOf<TestingChecklistMethodNode>()
        // val methodChecklistGenerator = MethodChecklistGenerationStrategy.create()
        methods.forEach { children.add(methodChecklistGenerator.generateChecklist(it)) }
        return TestingChecklistClassNode(methodName, children, psiElement)
    }
}
