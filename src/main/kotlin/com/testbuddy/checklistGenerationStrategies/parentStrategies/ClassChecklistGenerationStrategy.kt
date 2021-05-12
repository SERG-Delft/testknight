package com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.ChecklistGeneratorStrategy
import com.testbuddy.com.testbuddy.models.TestingChecklistClassNode

class ClassChecklistGenerationStrategy private constructor():
    ParentChecklistGeneratorStrategy<PsiClass, TestingChecklistClassNode> {

    companion object Factory {
        fun create(): ClassChecklistGenerationStrategy {
            return ClassChecklistGenerationStrategy()
        }
    }

    override fun generateChecklist(psiElement: PsiClass): TestingChecklistClassNode {
        //get all method children
        //call recursively on each method and save result to list
        //put this list in the class
        TODO()
    }
}
