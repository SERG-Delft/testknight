package com.testbuddy.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel

class SettingsConfigurable : BoundConfigurable("TestBuddy") {
    private var mySettingsComponent: SettingsComponent? = null

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

    override fun createPanel(): DialogPanel {
        mySettingsComponent = SettingsComponent()
        return mySettingsComponent!!.getComponent()
    }
}
