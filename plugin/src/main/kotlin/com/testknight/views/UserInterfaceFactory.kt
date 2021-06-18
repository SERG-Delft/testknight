package com.testknight.views

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class UserInterfaceFactory : ToolWindowFactory {
    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val userInterface = UserInterface(project)
        val contentManager = toolWindow.contentManager
        val content = contentManager.factory.createContent(userInterface.getContent(), null, false)
        toolWindow.contentManager.addContent(content)
    }
}
