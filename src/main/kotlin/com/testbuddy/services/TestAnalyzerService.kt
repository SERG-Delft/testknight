package com.testbuddy.com.testbuddy.services

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil

class TestAnalyzerService {

    /**
     * A set of recognized assertion names
     */
    private val assertionNames = setOf(
        "assertEquals",
        "assertTrue",
        "assertFalse",
        "assertNotNull",
        "assertNull",
        "assertSame",
        "assertNotSame"
    )

    private val testAnnotations = setOf("Test", "ParameterizedTest")

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

    /**
     * Takes a PsiMethod and returns a list of PsiElements pointing to each assertion parameter.
     *
     * @param psiMethod the PSI method
     * @return a list of PsiElements which correspond to all the assertion parameters in the test
     */
    fun getAssertionParameters(psiMethod: PsiMethod): List<PsiElement> {

        val res = arrayListOf<PsiElement>()

        PsiTreeUtil.findChildrenOfType(psiMethod, PsiMethodCallExpression::class.java)
            .forEach {
                if (assertionNames.contains(it.methodExpression.qualifiedName)) {
                    it.argumentList.expressions.forEach { res.add(it as PsiElement) }
                }
            }

        return res
    }
}
