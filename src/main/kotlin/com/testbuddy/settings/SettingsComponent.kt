package com.testbuddy.settings

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.panel

class SettingsComponent {

    var myPanel : DialogPanel
    val state = SettingsState()

    init {
        myPanel = panel {
        }
    }

    fun getComponent() : DialogPanel {
        return myPanel
    }
}