package com.testbuddy.listeners

import com.intellij.ide.BrowserUtil
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginStateListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.layout.panel
import com.testbuddy.settings.SettingsService
import javax.swing.Action
import javax.swing.JComponent

class WelcomeListener(val project: Project) : PluginStateListener {

    private var settingsState = SettingsService.instance.state

    /**
     * When the user installs the plugin, a Dialog should appear.
     */
    override fun install(descriptor: IdeaPluginDescriptor) {
        println("install")

        val installDialog = InstallDialog()

        val usePluginChoice = installDialog.showAndGet()
        if (usePluginChoice) {
            println(installDialog)
            settingsState.telemetrySettings.isEnabled = installDialog.getCheckboxValue()
        }
    }

    override fun uninstall(descriptor: IdeaPluginDescriptor) {

        print("uninstall")
    }

    private class InstallDialog : DialogWrapper(true) {

        private var checkbox = JBCheckBox("I ALLOW to send data!")

        init {
            init()
            title = "Welcome to the TestBuddy plugin"
            isOKActionEnabled = false
        }

        /**
         * Gets the value of the checkbox (the user want to sends data or not).
         *
         */
        fun getCheckboxValue(): Boolean {
            return checkbox.isSelected
        }

        /**
         * Create the content of the Dialog.
         */
        override fun createCenterPanel(): JComponent {
            val content = panel {

                titledRow("Terms and conditions for using the plugin") {
                    row {
                        label("Here we will put the description")
                    }
                }

                titledRow("Sending data") {

                    row {
                        checkbox()
                    }
                }

                noteRow("""Note with a link. <a href="http://github.com">Open source</a>""") {
                    BrowserUtil.browse(it)
                }
            }

            return content
        }

        /**
         * Create the actions for the Dialog and return them in form of an array.
         *
         * @return an array of Actions which represents the actions of the Dialog
         */
        override fun createActions(): Array<Action> {
            super.createDefaultActions()
            return arrayOf(InstallAction("Use Plugin", OK_EXIT_CODE))
        }

        private inner class InstallAction(name: String?, exitCode: Int) : DialogWrapperExitAction(name, exitCode)
    }
}
