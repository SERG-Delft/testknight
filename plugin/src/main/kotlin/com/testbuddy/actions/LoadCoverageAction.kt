package com.testbuddy.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.impl.ContentImpl
import com.intellij.ui.table.JBTable
import com.testbuddy.models.CoverageStatsObject
import com.testbuddy.services.CoverageDataService
import java.util.Vector
import javax.swing.JTabbedPane
import javax.swing.table.DefaultTableModel

class LoadCoverageAction : AnAction() {

    /**
     * Updates the Coverage tab to load the statistics from the testing suite.
     *
     * @param e Event received when the associated menu item is chosen.
     */
    @SuppressWarnings("MagicNumber")
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
        val table = coverageViewport.view as JBTable
        val serv = project.service<CoverageDataService>()

        serv.getDiffLines()

        val model = table.model as DefaultTableModel
        model.rowCount = 0 // Clear table

        for (x in serv.classCoveragesMap) {
            val vec = Vector<Any>()
            vec.add(x.key)
            val value = x.value

            val newLines = if (value.allLinesNow.isEmpty()) { 0 } else {
                (value.coveredNow.size.toFloat() / value.allLinesNow.size.toFloat() * 100).toInt()
            }
            val oldLines = if (value.allLinesPrev.isEmpty()) { 0 } else {
                (value.coveredPrev.size.toFloat() / value.allLinesPrev.size.toFloat() * 100).toInt()
            }

            vec.add(
                CoverageStatsObject(
                    value.coveredNow.size,
                    value.allLinesNow.size,
                    newLines,
                    newLines - oldLines
                )
            )

            vec.add("Diff")

            model.addRow(vec)
        }
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
