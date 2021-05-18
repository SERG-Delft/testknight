package com.testbuddy.com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil

object AssertionArgsStrategy : HighlightResolutionStrategy {

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

    override val priority: Int = 2

    override fun getElements(psiMethod: PsiMethod): List<PsiElement> {

        val res = arrayListOf<PsiElement>()

        PsiTreeUtil.findChildrenOfType(psiMethod, PsiMethodCallExpression::class.java)
            .forEach { methodCall ->
                if (assertionNames.contains(methodCall.methodExpression.qualifiedName)) {
                    methodCall.argumentList.expressions.forEach { res.add(it as PsiElement) }
                }
            }

        return res
    }
}
