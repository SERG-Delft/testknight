package com.testknight.listeners

import com.intellij.ide.BrowserUtil
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginStateListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.layout.panel
import com.testknight.settings.SettingsService
import javax.swing.Action
import javax.swing.JComponent

class WelcomeListener(val project: Project) : PluginStateListener {

    private var settingsState = SettingsService.instance.state

    /**
     * When the user installs the plugin, a Dialog should appear.
     */
    override fun install(descriptor: IdeaPluginDescriptor) {

        val installDialog = InstallDialog()

        val usePluginChoice = installDialog.showAndGet()
        if (usePluginChoice) {
            println(installDialog)
            settingsState.telemetrySettings.isEnabled = installDialog.getCheckboxValue()
        }
    }

    override fun uninstall(descriptor: IdeaPluginDescriptor) {
        // empty body for the uninstall
    }

    private class InstallDialog : DialogWrapper(true) {

        private var checkbox = JBCheckBox("I agree with sharing my usage data.")

        init {
            init()
            title = "TestKnight Plugin"
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

                titledRow("<html><b>Terms and conditions</b></html>") {
                    noteRow(
                        """The terms and conditions for using the plugin can be found """ +
                            """<a href="https://github.com/SERG-Delft/testknight/blob/master/PRIVACY.md">here</a>."""
                    ) {
                        BrowserUtil.browse(it)
                    }
                }

                titledRow("<html><b>Sending data</b></html>") {
                    row {
                        checkbox()
                    }
                }

                noteRow("""This plugin is <a href="https://github.com/SERG-Delft/testknight">open source</a>.""") {
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
