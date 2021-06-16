package com.testknight.highlightResolutionStrategies

import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.models.HighlightedTextData

object MagicNumberStrategy : HighlightResolutionStrategy {

    override val priority: Int = 1

    override val settingsName = "Highlight literals"

    override fun getElements(psiMethod: PsiMethod): List<HighlightedTextData> {
        return PsiTreeUtil.findChildrenOfType(psiMethod, PsiLiteralExpression::class.java)
            .map {
                HighlightedTextData(it)
            }
    }
}
