package com.testbuddy.settings

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.service
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.ui.DialogPanel
import com.testbuddy.services.CoverageHighlighterService
import com.testbuddy.services.GenerateTestCaseChecklistService

class SettingsConfigurable : BoundConfigurable("TestBuddy") {
    private lateinit var mySettingsComponent: SettingsComponent
    private val state = SettingsService.instance.state

    /**
     * Returns the visible name of the configurable component.
     * Note, that this method must return the display name
     * that is equal to the display name declared in XML
     * to avoid unexpected errors.
     *
     * @return the visible name of the configurable component
     */
    override fun getDisplayName(): String {
        return "TestBuddy"
    }

    /**
     * Indicates whether the Swing form was modified or not.
     * This method is called very often, so it should not take a long time.
     *
     * @return {@code true} if the settings were modified, {@code false} otherwise
     */
    override fun isModified(): Boolean {
        val state = SettingsService.instance.state.coverageSettings

        val colorChanged = mySettingsComponent.isColorModified(state)
        return (colorChanged || super.isModified())
    }

    /**
     * Stores the settings from the Swing form to the configurable component.
     * This method is called on EDT upon user's request.
     *
     * @throws ConfigurationException if values cannot be applied
     */
    @Throws(ConfigurationException::class)
    override fun apply() {
        super.apply()
        mySettingsComponent.applyCoverageColors(state.coverageSettings)
        ServiceManager.getService(GenerateTestCaseChecklistService::class.java).rebuildStrategies()

        // for each project apply the settings for project services
        ProjectManager.getInstance().openProjects.forEach {

            // refresh possibly changed highlight colors
            it.service<CoverageHighlighterService>().rebuildHighlights()

            if (!SettingsService.state.coverageSettings.showIntegratedView) {
                it.service<CoverageHighlighterService>().removeHighlights()
            }

            if (SettingsService.state.coverageSettings.showIntegratedView) {
                it.service<CoverageHighlighterService>().rebuildHighlights()
            }
        }
    }

    /**
     * Loads the settings from the configurable component to the Swing form.
     * This method is called on EDT immediately after the form creation or later upon user's request.
     */
    override fun reset() {
        super.reset()

        mySettingsComponent.resetCoverageColors(state.coverageSettings)
    }

    /**
     * Creates the setting panel to be shown.
     * reset() gets called after panel has been created.
     */
    override fun createPanel(): DialogPanel {
        mySettingsComponent = SettingsComponent()
        return mySettingsComponent.getComponent()
    }
}
