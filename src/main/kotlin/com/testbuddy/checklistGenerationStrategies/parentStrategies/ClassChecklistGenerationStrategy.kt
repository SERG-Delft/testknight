package com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiClass
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistClassNode

class ClassChecklistGenerationStrategy private constructor():
    ChecklistGeneratorStrategy<PsiClass, TestingChecklistClassNode> {

    companion object Factory {
        fun create(): ClassChecklistGenerationStrategy {
            return ClassChecklistGenerationStrategy()
        }
    }

    override fun generateChecklist(psiElement: PsiClass): TestingChecklistClassNode {
        //get all method children
        //val methods = PsiTreeUtil.findChildrenOfType(psiElement, PsiMethod::class.java)
        //val children = mutableListOf<TestingChecklistMethodNode>()
        //val methodStrategy = MethodChecklistGenerationStrategy.create()
        //methods.forEach {children.add(methodStrategy.generateChecklist(it))}
        //call recursively on each method and save result to list
        //put this list in the class
        TODO()
    }
}
