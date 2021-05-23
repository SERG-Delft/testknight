package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil

class TestAnalyzerService {

    /**
     * A list of all recognized test annotations
     */
    private val testAnnotations = setOf(
        "Test",
        "ParameterizedTest",
        "org.junit.jupiter.api.Test",
        "org.junit.jupiter.api.ParameterizedTest",
        "org.junit.Test",
        "org.junit.ParameterizedTest"
    )

    /**
     * Checks if a Psi class is a test class.
     *
     * @param psiClass the PsiClass to be examined.
     * @return true iff the given class contains a test method.
     */
    fun isTestClass(psiClass: PsiClass): Boolean {
        val methods = PsiTreeUtil.findChildrenOfType(psiClass, PsiMethod::class.java)
        methods.forEach { if (isTestMethod(it)) return true }
        return false
    }

    /**
     * Checks if a PSI method is a test method.
     *
     * @param method the PSI method
     * @return true iff the method is a test method
     */
    fun isTestMethod(method: PsiMethod): Boolean {
        val annotations = method.annotations.map { it.qualifiedName }.toSet()
        return annotations.intersect(testAnnotations).isNotEmpty()
    }
}
