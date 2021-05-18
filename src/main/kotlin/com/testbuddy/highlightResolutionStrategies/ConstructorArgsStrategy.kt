package com.testbuddy.com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiConstructorCall
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil

object ConstructorArgsStrategy : HighlightResolutionStrategy {

    override val priority = 1

    override fun getElements(psiMethod: PsiMethod): List<PsiElement> {
        val res = arrayListOf<PsiElement>()

        PsiTreeUtil.findChildrenOfType(psiMethod, PsiConstructorCall::class.java)
            .forEach { constructorCall ->
                constructorCall.argumentList?.expressions?.forEach { res.add(it as PsiElement) }
            }

        return res
    }
}
