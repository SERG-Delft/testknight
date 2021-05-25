package com.testbuddy.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nullable
import javax.swing.JComponent


class SettingsConfigurable : Configurable {
    private var mySettingsComponent: SettingsComponent? = null
    private var settingsService = SettingsService.instance


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

    @Nullable
    override fun createComponent(): JComponent {
        mySettingsComponent = SettingsComponent()
        return mySettingsComponent!!.getComponent()
    }


    //Inform when UI component got modified compared to saved state.
    override fun isModified(): Boolean {
        return mySettingsComponent!!.state.myFlag
    }


    //Save from component into file
    override fun apply() {
         mySettingsComponent!!.state.copyTo(settingsService.state!!)
    }

    //Load from file into component
    override fun reset() {
        settingsService.state ?: return
        settingsService.state!!.copyTo(mySettingsComponent!!.state)
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }


}