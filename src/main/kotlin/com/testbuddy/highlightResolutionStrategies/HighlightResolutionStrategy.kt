package com.testbuddy.com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod

interface HighlightResolutionStrategy {

    /**
     * Represents the priority of the strategy. Lower is higher priority
     */
    val priority: Int

    /**
     * Gets a list of PSI elements to be highlighted from a PSI method
     *
     * @param psiMethod the method
     * @return a list of PSI elements to be highlighted
     */
    fun getElements(psiMethod: PsiMethod): List<PsiElement>
}
