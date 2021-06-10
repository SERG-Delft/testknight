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
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent) {
        if (SampleDialogWrapper().showAndGet()) {
            mySettingsComponent.typeCaseTreeInfo.clear()
            for (item in SettingsState().checklistSettings.typeCaseMap) {
                mySettingsComponent.typeCaseTreeInfo[item.key] = item.value.toMutableList() // Creates a copy
            }
            mySettingsComponent.isTypeCaseTreeModified = true
            mySettingsComponent.typeCaseTreeModel.reload()
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = true
    }
    fun init(component: SettingsComponent) {
        mySettingsComponent = component
    }

    class SampleDialogWrapper : DialogWrapper(true) {
        init {
            init()
            title = "Reset Tree"
        }

        override fun createCenterPanel(): JComponent {
            return panel {
                row {
                    val icon = UIManager.getIcon("OptionPane.warningIcon")
                    if (icon != null) {
                        JBLabel(icon)()
                    }
                    label("This will reset the parameter suggestion tree to its default values. You will lose all your custom modifications.")
                }
            }
        }
    }
}
