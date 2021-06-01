package com.testbuddy.actions

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.view.CoverageView
import com.intellij.coverage.view.CoverageViewManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.impl.ContentImpl
import javax.swing.JTabbedPane

class LoadCoverageAction : AnAction() {

    /**
     * Updates the Coverage tab to load the statistics from the testing suite.
     *
     * @param e Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(e: AnActionEvent) {

        val project = e.project!!
        val window: ToolWindow? = ToolWindowManager.getInstance(project).getToolWindow("TestBuddy")
        val tabbedPane = (
            (window!!.contentManager.contents[0] as ContentImpl)
                .component as JTabbedPane
            )
        val coverageTab = tabbedPane.getComponentAt(2) as JBPanelWithEmptyText
        val coverageScroll = coverageTab.getComponent(1) as JBScrollPane
        val coverageViewport = coverageScroll.viewport

        val content = try {
            val instance = CoverageDataManager.getInstance(project) ?: return
            val stateBean = CoverageViewManager.StateBean()
            CoverageView(project, instance, stateBean)
        } catch (e: IllegalArgumentException) {
            println(e)
            return
        }

        coverageViewport.view = content
    }

    /**
     * Determines whether this menu item is available for the current context.
     * Requires a project to be open and psiFile and Editor to be accessible from the action event.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    override fun update(e: AnActionEvent) {
        // Set the availability based on whether the project, psiFile and editor is not null
        e.presentation.isEnabled = e.project != null
    }
}
