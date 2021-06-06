package com.testbuddy.settings

import com.intellij.ui.ColorPanel
import com.intellij.ui.ColorUtil
import com.testbuddy.extensions.TestBuddyTestCase
import javafx.scene.paint.Color
import junit.framework.TestCase
import org.junit.jupiter.api.Test

class SettingsComponentTest : TestBuddyTestCase() {

    @Test
    fun testInitialisation() {
        val settingsComponent = SettingsComponent()
        var actualPanel = settingsComponent.getComponent()
        TestCase.assertNotNull(actualPanel)
    }

    @Test
    fun testApplyCoverageColorsAddedColor() {
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
    fun testApplyCoverageColorsDeletedColor() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.addedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.deletedColor.selectedColor = java.awt.Color.BLACK
        settingsComponent.tracedColor.selectedColor = java.awt.Color.BLUE
        settingsComponent.applyCoverageColors(coverageColors)
        TestCase.assertEquals(
            coverageColors.deletedColor,
            settingsComponent.deletedColor.selectedColor
                ?.let { SettingsService.toColorHex(it) }
        )
    }

    @Test
    fun testApplyCoverageColorsTracedColor() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.addedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.deletedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.tracedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.applyCoverageColors(coverageColors)
        TestCase.assertEquals(
            coverageColors.tracedColor,
            settingsComponent.tracedColor.selectedColor
                ?.let { SettingsService.toColorHex(it) }
        )
    }

    @Test
    fun testApplyColorsAddedColorNull() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        TestCase.assertEquals(coverageColors.addedColor, ColorUtil.toHex(java.awt.Color.GREEN))
    }

    @Test
    fun testApplyColorsDeletedColorNull() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        TestCase.assertEquals(coverageColors.deletedColor, ColorUtil.toHex(java.awt.Color.RED))
    }

    @Test
    fun testApplyColorsTracedColorNull() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        TestCase.assertEquals(coverageColors.tracedColor, ColorUtil.toHex(java.awt.Color.CYAN))
    }

    @Test
    fun testIsColorModifiedAllValuesSet() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.addedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.deletedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.tracedColor.selectedColor = java.awt.Color.WHITE
        assertTrue(settingsComponent.isColorModified(coverageColors))
    }

    @Test
    fun testIsColorModifiedNullAddedColor() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.deletedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.tracedColor.selectedColor = java.awt.Color.WHITE
        assertFalse(settingsComponent.isColorModified(coverageColors))
    }

    @Test
    fun testIsColorModifiedNullDeletedColor() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.addedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.tracedColor.selectedColor = java.awt.Color.WHITE
        assertFalse(settingsComponent.isColorModified(coverageColors))
    }

    @Test
    fun testIsColorModifiedNullTracedColor() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.addedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.deletedColor.selectedColor = java.awt.Color.WHITE
        assertFalse(settingsComponent.isColorModified(coverageColors))
    }

    @Test
    fun testIsColorModifiedAddedDifferent() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.addedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.deletedColor.selectedColor = java.awt.Color.RED
        settingsComponent.tracedColor.selectedColor = java.awt.Color.CYAN
        assertTrue(settingsComponent.isColorModified(coverageColors))
    }

    @Test
    fun testIsColorModifiedDeletedDifferent() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.addedColor.selectedColor = java.awt.Color.GREEN
        settingsComponent.deletedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.tracedColor.selectedColor = java.awt.Color.CYAN
        assertTrue(settingsComponent.isColorModified(coverageColors))
    }

    @Test
    fun testIsColorModifiedTracedDifferent() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.addedColor.selectedColor = java.awt.Color.GREEN
        settingsComponent.deletedColor.selectedColor = java.awt.Color.RED
        settingsComponent.tracedColor.selectedColor = java.awt.Color.WHITE
        assertTrue(settingsComponent.isColorModified(coverageColors))
    }

    @Test
    fun testIsColorModifiedAllDifferent() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor = ColorPanel()
        settingsComponent.deletedColor = ColorPanel()
        settingsComponent.tracedColor = ColorPanel()
        settingsComponent.addedColor.selectedColor = java.awt.Color.GREEN
        settingsComponent.deletedColor.selectedColor = java.awt.Color.RED
        settingsComponent.tracedColor.selectedColor = java.awt.Color.CYAN
        assertFalse(settingsComponent.isColorModified(coverageColors))
    }

    @Test
    fun testResetCoverageColors() {
        val coverageColors = CoverageSettings()
        val settingsComponent = SettingsComponent()
        settingsComponent.addedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.deletedColor.selectedColor = java.awt.Color.WHITE
        settingsComponent.tracedColor.selectedColor = java.awt.Color.WHITE
        val covAddedColor = java.awt.Color.GREEN
        val covDeletedColor = java.awt.Color.RED
        val covTracedColor = java.awt.Color.CYAN
        settingsComponent.resetCoverageColors(coverageColors)
        TestCase.assertEquals(settingsComponent.addedColor.selectedColor, covAddedColor)
        TestCase.assertEquals(settingsComponent.deletedColor.selectedColor, covDeletedColor)
        TestCase.assertEquals(settingsComponent.tracedColor.selectedColor, covTracedColor)
    }
}
