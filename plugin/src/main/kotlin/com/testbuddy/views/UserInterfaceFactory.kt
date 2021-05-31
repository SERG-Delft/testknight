package com.testbuddy.com.testbuddy.views

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.testbuddy.com.testbuddy.services.CoverageDataService
import org.jetbrains.annotations.NotNull

class UserInterfaceFactory : ToolWindowFactory {
    /**
     * Create the tool window content.
     *
     * @param project    current project
     * @param toolWindow current tool window
     */
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val userInterface = UserInterface(project)
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(userInterface.getContent(), "", false)
        toolWindow.contentManager.addContent(content)
        project.messageBus.connect().subscribe(
            VirtualFileManager.VFS_CHANGES,
            object : BulkFileListener {
                override fun after(@NotNull events: List<VFileEvent?>) {

                    val service = project.service<CoverageDataService>()
                    val map = service.classCoveragesMap
                    events.forEach {
                        if (it != null && it.file != null) {
                            if (map.containsKey(it.file!!.nameWithoutExtension)) {
                                val coverageDataService = project.service<CoverageDataService>()
                                coverageDataService.resetCurrentDataAndMap()
                                service.setIsDiffAvailable(false)
                            }
                        }
                    }

                    println(events.size)
                }
            }
        )
    }
}
