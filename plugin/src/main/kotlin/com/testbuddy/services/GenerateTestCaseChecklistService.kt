package com.testbuddy.com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.ClassChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.checklistGenerationStrategies.parentStrategies.MethodChecklistGenerationStrategy
import com.testbuddy.com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testbuddy.com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistMethodNode

class GenerateTestCaseChecklistService {

    private val testAnalyzerService = TestAnalyzerService()

    private var classStrategy = ClassChecklistGenerationStrategy.create()
    private var methodStrategy = MethodChecklistGenerationStrategy.create()

    /**
     * Generate a checklist for a given class.
     *
     * @param psiClass the class
     * @return a TestingChecklistClassNode containing the checklists for each method
     */
    fun generateClassChecklistFromClass(psiClass: PsiClass): TestingChecklistClassNode {
        return classStrategy.generateChecklist(psiClass)
    }

    /**
     * Generate a checklist for a given method.
     *
     * @param psiMethod the psiMethod
     * @return a TestingChecklistClassNode containing only the method specified in the parameter
     */
    fun generateClassChecklistFromMethod(psiMethod: PsiMethod): TestingChecklistClassNode {

        val methodItem = methodStrategy.generateChecklist(psiMethod)
        val parentClass = PsiTreeUtil.getParentOfType(psiMethod, PsiClass::class.java) as PsiClass

        if (testAnalyzerService.isTestMethod(psiMethod)) return TestingChecklistClassNode(
            parentClass.name!!,
            mutableListOf(),
            parentClass
        )

        return TestingChecklistClassNode(
            parentClass.name!!,
            mutableListOf(methodItem),
            parentClass
        )
    }

    /**
     * Generate a checklist given a psiMethod.
     *
     * @param psiMethod the PSI method
     * @return a TestingChecklistMethodNode containing
     */
    fun generateMethodChecklist(psiMethod: PsiMethod): TestingChecklistMethodNode {
        return methodStrategy.generateChecklist(psiMethod)
    }

    /**
     * Re-creates the class and method strategies. This method is called whenever settings are modified
     */
    fun rebuildStrategies() {
        classStrategy = ClassChecklistGenerationStrategy.create()
        methodStrategy = MethodChecklistGenerationStrategy.create()
    }
}
