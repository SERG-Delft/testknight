package com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiConstructorCall
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.models.HighlightedTextData

object ConstructorArgsStrategy : HighlightResolutionStrategy {

    override val priority = 1

    override val settingsName = "Highlight constructor arguments"

    override fun getElements(psiMethod: PsiMethod): List<HighlightedTextData> {
        val res = arrayListOf<HighlightedTextData>()

        PsiTreeUtil.findChildrenOfType(psiMethod, PsiConstructorCall::class.java)
            .forEach { constructorCall ->
                constructorCall.argumentList?.expressions?.forEach {
                    res.add(HighlightedTextData(it))
                }
            }

        return res
    }
}
