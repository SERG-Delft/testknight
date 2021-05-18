package com.testbuddy.services

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiConstructorCall
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

    private val testAnnotations = setOf(
        "Test", "ParameterizedTest", "org.junit.jupiter.api.Test",
        "org.junit.jupiter.api.ParameterizedTest", "org.junit.Test", "org.junit.ParameterizedTest"
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

    /**
     * Takes a PsiMethod and returns a list of PsiElements pointing to each assertion argument
     *
     * @param psiMethod the PSI method
     * @return a list of PsiElements which correspond to all the assertion arguments in the test
     */
    fun getAssertionArgs(psiMethod: PsiMethod): List<PsiElement> {

        val res = arrayListOf<PsiElement>()

        PsiTreeUtil.findChildrenOfType(psiMethod, PsiMethodCallExpression::class.java)
            .forEach { methodCall ->
                if (assertionNames.contains(methodCall.methodExpression.qualifiedName)) {
                    methodCall.argumentList.expressions.forEach { res.add(it as PsiElement) }
                }
            }

        return res
    }

    /**
     * Takes a PsiMethod and returns a list of PsiElements pointing to each constructor argument
     *
     * @param psiMethod the PSI method
     * @return a list of PsiElements which correspond to all the constructor args in the test
     */
    fun getConstructorArgs(psiMethod: PsiMethod): List<PsiElement> {

        val res = arrayListOf<PsiElement>()

        PsiTreeUtil.findChildrenOfType(psiMethod, PsiConstructorCall::class.java)
            .forEach { constructorCall ->
                constructorCall.argumentList?.expressions?.forEach { res.add(it as PsiElement) }
            }

        return res
    }
}
