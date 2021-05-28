package com.testbuddy.settings

import java.awt.Color

data class SettingsState(
    var telemetrySettings: TelemetrySettings = TelemetrySettings(),
    var testListSettings: TestListSettings = TestListSettings(),
    var checklistSettings: ChecklistSettings = ChecklistSettings(),
    var coverageSettings: CoverageSettings = CoverageSettings()
)

data class TelemetrySettings(var isEnabled: Boolean = false)

data class TestListSettings(
    var autoUpdateUI: Boolean = true,
    var highlightStrategies: MutableMap<String, Boolean> = mutableMapOf()
)

data class ChecklistSettings(
    var coverageCriteria: String = "MCDC",
    var checklistStrategies: MutableMap<String, Boolean> = mutableMapOf(),
    var typeCaseMap: MutableMap<String, List<String>> = mutableMapOf(),
    var showGutterIcons: Boolean = true,
    var gotoChecklistItem: Boolean = true,
    var highlightChecklistItem: Boolean = true,
    var highlightColor: Color = Color.LIGHT_GRAY
)

data class CoverageSettings(
    var includedColor: Color = Color.GREEN,
    var deletedColor: Color = Color.RED,
    var diffIncludedColor: Color = Color.GREEN,
    var diffExcludedColor: Color = Color.RED
)
