package com.testknight.settings

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

/**
 * An enum class to wrap around the textual values of
 * the settings for the checklist generation
 */
enum class ChecklistSettingsValues(val valueName: String) {
    IF_STATEMENT("If Statement"),
    SWITCH_STATEMENT("Switch Statement"),
    TRY_STATEMENT("Try Statement"),
    PARAMETER_LIST("Parameter List"),
    WHILE_STATEMENT("While Statement"),
    FOR_STATEMENT("For Statement"),
    DO_WHILE_STATEMENT("Do While Statement"),
    FOREACH_STATEMENT("Foreach Statement"),
    THROW_STATEMENT("Throw Statement"),
    TERNARY_OPERATOR("Ternary Operator")
}

data class ChecklistSettings(
    var coverageCriteria: String = "MC/DC",
    var checklistStrategies: MutableMap<String, Boolean> = mutableMapOf(
        ChecklistSettingsValues.IF_STATEMENT.valueName to true,
        ChecklistSettingsValues.SWITCH_STATEMENT.valueName to true,
        ChecklistSettingsValues.TRY_STATEMENT.valueName to true,
        ChecklistSettingsValues.PARAMETER_LIST.valueName to true,
        ChecklistSettingsValues.WHILE_STATEMENT.valueName to true,
        ChecklistSettingsValues.FOR_STATEMENT.valueName to true,
        ChecklistSettingsValues.DO_WHILE_STATEMENT.valueName to true,
        ChecklistSettingsValues.FOREACH_STATEMENT.valueName to true,
        ChecklistSettingsValues.THROW_STATEMENT.valueName to true,
        ChecklistSettingsValues.TERNARY_OPERATOR.valueName to true
    ),
    var paramSuggestionMap: MutableMap<String, MutableList<String>> = mutableMapOf(
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
