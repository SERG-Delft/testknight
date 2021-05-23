package com.testbuddy.com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiConstructorCall
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import com.testbuddy.models.HighlightedTextData

object ConstructorArgsStrategy : HighlightResolutionStrategy {

    override val priority = 1

    override fun getElements(psiMethod: PsiMethod): List<HighlightedTextData> {
        val res = arrayListOf<HighlightedTextData>()

        PsiTreeUtil.findChildrenOfType(psiMethod, PsiConstructorCall::class.java)
            .forEach { constructorCall ->
                constructorCall.argumentList?.expressions?.forEach {
                    res.add(HighlightedTextData(it.startOffset, it.endOffset, it.text))
                }
            }

        return res
    }
}
