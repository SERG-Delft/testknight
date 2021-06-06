package com.testbuddy.settings


import com.intellij.ui.ColorPanel
import com.intellij.ui.ColorUtil
import com.testbuddy.extensions.TestBuddyTestCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import javafx.scene.paint.Color
import junit.framework.TestCase
import org.junit.jupiter.api.Test

class SettingsComponentTest : TestBuddyTestCase(){

    @Test
    fun testInitialisation() {
        val settingsComponent = SettingsComponent()
        var actualPanel = settingsComponent.getMyPanel()
        TestCase.assertNotNull(actualPanel)
    }

}