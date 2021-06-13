package com.testbuddy.actions.testcases

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction

class TestListTraceabilityAction : ToggleAction() {

    private var selected = false

    /**
     * Returns the state value (true/false)
     *
     * @param e the AnActionEvent which triggers the action.
     * @return true if the action is selected, false otherwise
     */
    override fun isSelected(e: AnActionEvent): Boolean {
        return selected
    }

    /**
     * Sets the selected variable to the value of the state.
     *
     * @param e the AnActionEvent which changes the selected variable.
     * @param state the new selected state of the selected variable.
     */
    override fun setSelected(e: AnActionEvent, state: Boolean) {
        selected = state
    }
}
