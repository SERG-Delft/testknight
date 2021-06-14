package com.testbuddy.actions.settings

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBLabel
import com.intellij.ui.layout.panel
import com.testbuddy.settings.SettingsComponent
import com.testbuddy.settings.SettingsState
import javax.swing.JComponent
import javax.swing.UIManager

class ResetTreeAction : AnAction() {
    private lateinit var mySettingsComponent: SettingsComponent

    /**
     * If the OK (or equivalent) button is selected from the warning dialog,
     * The ParamSuggestionTree will get reset to default values.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent) {
        if (ResetTreeWarningDialog().showAndGet()) {
            mySettingsComponent.paramSuggestionTreeInfo.clear()
            for (item in SettingsState().checklistSettings.paramSuggestionMap) {
                mySettingsComponent.paramSuggestionTreeInfo[item.key] = item.value.toMutableList() // Creates a copy
            }
            // Modified regardless of state
            mySettingsComponent.paramSuggestionModified = true
            mySettingsComponent.paramSuggestionTreeModel.reload()
        }
    }

    /**
     * Updates the state of the action.
     * The action is enabled all the time.
     *
     * @param e Carries information on the invocation place
     */
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = true
    }

    /**
     * Sets the mySettingsComponent attribute which is used to update the tree.
     */
    fun init(component: SettingsComponent) {
        mySettingsComponent = component
    }

    /**
     * Dialog component which shows the warning message when the user tries to reset to default values.
     */
    inner class ResetTreeWarningDialog : DialogWrapper(true) {
        init {
            init()
            title = "Reset Parameter Suggestions"
        }

        override fun createCenterPanel(): JComponent {
            return panel {
                row {
                    val icon = UIManager.getIcon("OptionPane.warningIcon")
                    if (icon != null) {
                        JBLabel(icon)()
                    }
                    label(
                        "Doing this operation will reset the parameter suggestion list to it's default values. " +
                            "You will lose all your modification information."
                    )
                }
            }
        }
    }
}
