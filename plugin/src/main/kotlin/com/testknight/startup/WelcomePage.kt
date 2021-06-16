package com.testknight.startup

import com.intellij.ide.plugins.PluginInstaller
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.testknight.listeners.WelcomeListener

class WelcomePage : StartupActivity {
    override fun runActivity(project: Project) {
        PluginInstaller.addStateListener(WelcomeListener(project))
    }
}
