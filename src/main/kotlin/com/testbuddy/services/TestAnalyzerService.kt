package com.testbuddy.com.testbuddy.services

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil

class TestAnalyzerService {

    private val assertionNames = setOf(
        "assertEquals",
        "assertTrue",
        "org.junit.jupiter.api.Assertions.assertTrue",
        "org.junit.jupiter.api.Assertions.assertEquals"
    )

    /**
     * Takes a PsiMethod and returns a list of PsiElements pointing to each assertion parameter.
     *
     * @param psiMethod the PSI method
     * @return a list of PsiElements which correspond to all the assertion parameters in the test
     */
    fun getAssertionParameters(psiMethod: PsiMethod): List<PsiElement> {

        val res = arrayListOf<PsiElement>()

        PsiTreeUtil.findChildrenOfType(psiMethod, PsiMethodCallExpression::class.java)
            .filter { assertionNames.contains(it.methodExpression.qualifiedName) }
            .forEach { it -> it.argumentList.expressions.forEach { res.add(it as PsiElement) } }

        return res
    }
}
