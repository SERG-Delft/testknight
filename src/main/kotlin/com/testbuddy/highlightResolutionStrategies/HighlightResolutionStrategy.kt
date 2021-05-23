package com.testbuddy.com.testbuddy.highlightResolutionStrategies

import com.intellij.psi.PsiMethod
import com.testbuddy.models.HighlightedTextData

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
    fun getElements(psiMethod: PsiMethod): List<HighlightedTextData>
}
