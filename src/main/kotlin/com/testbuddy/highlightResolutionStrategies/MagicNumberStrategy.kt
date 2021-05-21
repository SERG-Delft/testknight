package com.testbuddy.com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil

object MagicNumberStrategy : HighlightResolutionStrategy {

    override val priority: Int = 1

    override fun getElements(psiMethod: PsiMethod): List<PsiElement> {

        return PsiTreeUtil.findChildrenOfType(psiMethod, PsiLiteralExpression::class.java)
            .map { it as PsiExpression }
    }
}
