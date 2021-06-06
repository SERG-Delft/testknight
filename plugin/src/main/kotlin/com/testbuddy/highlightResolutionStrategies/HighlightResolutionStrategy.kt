package com.testbuddy.highlightResolutionStrategies

import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiMethod
import com.testbuddy.models.HighlightedTextData
import com.testbuddy.settings.SettingsService

interface HighlightResolutionStrategy {

    /**
     * Represents the priority of the strategy. Lower is higher priority
     */
    val priority: Int

    val settingsName: String

    /**
     * @return true if the strategy is enabled in the settings
     */
    fun isEnabled() = ApplicationManager
        .getApplication()
        .getService(SettingsService::class.java)
        .state
        .testListSettings.highlightStrategies[settingsName]!!

    /**
     * Gets a list of PSI elements to be highlighted from a PSI method
     *
     * @param psiMethod the method
     * @return a list of PSI elements to be highlighted
     */
    fun getElements(psiMethod: PsiMethod): List<HighlightedTextData>
}
