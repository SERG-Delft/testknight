package com.testbuddy.settings

import com.intellij.ui.ColorUtil
import java.awt.Color
import java.util.UUID

/**
 * Data structure for storing the settings.
 */
data class SettingsState(
    var telemetrySettings: TelemetrySettings = TelemetrySettings(),
    var testListSettings: TestListSettings = TestListSettings(),
    var checklistSettings: ChecklistSettings = ChecklistSettings(),
    var coverageSettings: CoverageSettings = CoverageSettings(),
    var userId: String = UUID.randomUUID().toString()
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
    var typeCaseMap: MutableMap<String, MutableList<String>> = mutableMapOf(
        "byte" to mutableListOf("Byte.MAX_VALUE", "Byte.MIN_VALUE"),
        "short" to mutableListOf("Short.MAX_VALUE", "Short.MIN_VALUE"),
        "int" to mutableListOf("1", "0", "Integer.MAX_VALUE", "Integer.MIN_VALUE", "-42"),
        "long" to mutableListOf("0"),
        "float" to mutableListOf("3.14", "0.0", "-3.14", "1.0", "0.00000042", "Float.MAX_VALUE", "Float.MIN_VALUE"),
        "double" to mutableListOf("Double.MAX_VALUE", "Double.MIN_VALUE"),
        "char" to mutableListOf("a", "α", "∅"),
        "boolean" to mutableListOf("true", "false"),
        "String" to mutableListOf("\"\"", "\"a\"", "\"hello world\"", "\"καλήν εσπέραν άρχοντες\"", "\"€\"", "\"₹\""),
        "byte[]" to mutableListOf("[]", "null"),
        "short[]" to mutableListOf("[]", "null"),
        "int[]" to mutableListOf("null", "[1,2,3,4]", "[4,3,2,1]", "[]"),
        "long[]" to mutableListOf("[]", "null"),
        "float[]" to mutableListOf("[]", "null"),
        "double[]" to mutableListOf("[]", "null"),
        "char[]" to mutableListOf("[]", "null"),
        "boolean[]" to mutableListOf("[]", "null"),
        "String[]" to mutableListOf("[]", "null")
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
