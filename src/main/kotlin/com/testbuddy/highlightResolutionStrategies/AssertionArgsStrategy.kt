package com.testbuddy.com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.models.HighlightedTextData

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

    override fun getElements(psiMethod: PsiMethod): List<HighlightedTextData> {

        val res = arrayListOf<HighlightedTextData>()

        PsiTreeUtil.findChildrenOfType(psiMethod, PsiMethodCallExpression::class.java)
            .forEach { methodCall ->
                if (assertionNames.contains(methodCall.methodExpression.qualifiedName)) {
                    methodCall.argumentList.expressions.forEach {
                        res.add(HighlightedTextData(it))
                    }
                }
            }

        return res
    }
}
