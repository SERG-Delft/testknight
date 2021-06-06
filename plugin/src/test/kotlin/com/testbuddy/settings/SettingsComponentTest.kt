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

    @Test
    fun testApplyCoverageColorsAddedColor(){
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.addedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.deletedColor.selectedColor = java.awt.Color.BLACK
        settingsComponent.tracedColor.selectedColor = java.awt.Color.BLUE
        settingsComponent.applyCoverageColors(coverageColors)
        TestCase.assertEquals(coverageColors.addedColor, settingsComponent.addedColor.selectedColor?.let { SettingsService.toColorHex(it) })
    }

    @Test
    fun testApplyCoverageColorsDeletedColor(){
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.addedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.deletedColor.selectedColor = java.awt.Color.BLACK
        settingsComponent.tracedColor.selectedColor = java.awt.Color.BLUE
        settingsComponent.applyCoverageColors(coverageColors)
        TestCase.assertEquals(coverageColors.deletedColor, settingsComponent.deletedColor.selectedColor?.
        let { SettingsService.toColorHex(it) })
    }

    @Test
    fun testApplyCoverageColorsTracedColor(){
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.addedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.deletedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.tracedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.applyCoverageColors(coverageColors)
        TestCase.assertEquals(coverageColors.tracedColor, settingsComponent.tracedColor.selectedColor?.
        let { SettingsService.toColorHex(it) })
    }

}