package com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.models.TestingChecklistClassNode
import com.testbuddy.com.testbuddy.models.TestingChecklistMethodNode

class ClassChecklistGenerationStrategy private constructor(
    private val methodChecklistGenerator: ParentChecklistGeneratorStrategy<PsiMethod, TestingChecklistMethodNode>
) :
    ParentChecklistGeneratorStrategy<PsiClass, TestingChecklistClassNode> {

    // the factory currently just call the constructor
    // but we will use it in the future to build the strategy based on the use configuration.
    companion object Factory {
        /**
         * Creates a new ClassChecklistGenerationStrategy.
         *
         * @return a ClassChecklistGenerationStrategy
         */
        fun create(
            methodChecklistGenerator: ParentChecklistGeneratorStrategy<PsiMethod, TestingChecklistMethodNode>
        ): ClassChecklistGenerationStrategy {
            return ClassChecklistGenerationStrategy(methodChecklistGenerator)
        }
    }

    /**
     * Generates the checklist for a given class.
     *
     * @param psiClass the PsiClass to generate the checklist on.
     * @return a TestingChecklistClassNode object representing the checklist for that class.
     */
    override fun generateChecklist(psiClass: PsiClass): TestingChecklistClassNode {
        val methodName = psiClass.name ?: "No class name found"
        val methods = PsiTreeUtil.findChildrenOfType(psiClass, PsiMethod::class.java)
        val children = mutableListOf<TestingChecklistMethodNode>()
        // val methodChecklistGenerator = MethodChecklistGenerationStrategy.create()
        methods.forEach { children.add(methodChecklistGenerator.generateChecklist(it)) }
        return TestingChecklistClassNode(methodName, children, psiClass)
    }
}
