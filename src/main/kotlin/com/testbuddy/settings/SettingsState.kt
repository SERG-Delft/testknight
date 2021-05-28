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
    var typeCaseMap: MutableMap<String, List<String>> = mutableMapOf(
        "byte" to listOf("Byte.MAX_VALUE", "Byte.MIN_VALUE"),
        "short" to listOf("Short.MAX_VALUE", "Short.MIN_VALUE"),
        "int" to listOf("1", "0", "Integer.MAX_VALUE", "Integer.MIN_VALUE", "-42"),
        "long" to listOf("0"),
        "float" to listOf("3.14", "0.0", "-3.14", "1.0", "0.00000042", "Float.MAX_VALUE", "Float.MIN_VALUE"),
        "double" to listOf("Double.MAX_VALUE", "Double.MIN_VALUE"),
        "char" to listOf("a", "α", "∅"),
        "boolean" to listOf("true", "false"),
        "String" to listOf("\"\"", "\"a\"", "\"hello world\"", "\"καλήν εσπέραν άρχοντες\"", "\"€\"", "\"₹\""),
        "byte[]" to listOf("[]", "null"),
        "short[]" to listOf("[]", "null"),
        "int[]" to listOf("null", "[1,2,3,4]", "[4,3,2,1]", "[]"),
        "long[]" to listOf("[]", "null"),
        "float[]" to listOf("[]", "null"),
        "double[]" to listOf("[]", "null"),
        "char[]" to listOf("[]", "null"),
        "boolean[]" to listOf("[]", "null"),
        "String[]" to listOf("[]", "null")
    ),
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
