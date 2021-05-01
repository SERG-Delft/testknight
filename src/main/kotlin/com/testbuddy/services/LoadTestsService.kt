package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.models.TestCaseData

class LoadTestsService {

    private val testAnnotations = setOf("Test", "ParameterizedTest")

    /**
     * Extracts all the test methods from a PSI file.
     *
     * @param file the PSI file
     * @return a list of TestCaseData elements representing the test cases
     */
    fun getTests(file: PsiFile): List<TestCaseData> {
        val methods = PsiTreeUtil.findChildrenOfType(file, PsiMethod::class.java)
        return methods.filter(this::isTestMethod).map { TestCaseData(it.name, (it.parent as PsiClass).name ?: "", it) }
    }

    /**
     * Checks if a PSI method is a test method.
     *
     * @param method the PSI method
     * @return true iff the method is a test method
     */
    private fun isTestMethod(method: PsiMethod): Boolean {
        val annotations = method.annotations.map { it.qualifiedName }.toSet()
        return annotations.intersect(testAnnotations).isNotEmpty()
    }
}
