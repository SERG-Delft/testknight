package com.testbuddy.com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import com.testbuddy.models.HighlightedTextData

object MagicNumberStrategy : HighlightResolutionStrategy {

    override val priority: Int = 1

    override fun getElements(psiMethod: PsiMethod): List<HighlightedTextData> {
        return PsiTreeUtil.findChildrenOfType(psiMethod, PsiLiteralExpression::class.java)
            .map { HighlightedTextData(it.startOffset, it.endOffset, it.text) }
    }
}
