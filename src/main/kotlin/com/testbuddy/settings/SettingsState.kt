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
    var highlightStrategies: MutableMap<String, Boolean> = mutableMapOf(
        "Constructor" to true,
        "Literals" to true,
        "Assertions" to true,
        "Highlight inside quotes" to true
    )
)

data class ChecklistSettings(
    var coverageCriteria: String = "MCDC",
    var checklistStrategies: MutableMap<String, Boolean> = mutableMapOf(
        "If Statement" to true,
        "Switch Statement" to true,
        "Try Statement" to true,
        "Parameter List" to true,
        "While Statement" to true,
        "For Statement" to true,
        "Do While Statement" to true,
        "Foreach Statement" to true,
        "Throw Statement" to true,
        "Conditional Expression" to true
    ),
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
