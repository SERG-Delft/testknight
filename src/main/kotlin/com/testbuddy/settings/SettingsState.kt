package com.testbuddy.settings

import com.intellij.ui.ColorUtil
import java.awt.Color

/**
 * Data structure for storing the settings.
 */
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
        "Highlight constructor arguments" to true,
        "Highlight literals" to true,
        "Highlight assertion statements" to true,
        "Highlight string inside quotes" to true
    )
)

data class ChecklistSettings(
    var coverageCriteria: String = "MC/DC",
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
        "Ternary Operator" to true
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
    var highlightColor: String = ColorUtil.toHex(Color.LIGHT_GRAY)
)

data class CoverageSettings(
    var addedColor: String = ColorUtil.toHex(Color.GREEN),
    var deletedColor: String = ColorUtil.toHex(Color.RED),
    var tracedColor: String = ColorUtil.toHex(Color.CYAN),
    var showIntegratedView: Boolean = true
)
