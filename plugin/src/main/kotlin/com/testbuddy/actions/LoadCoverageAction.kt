package com.testbuddy.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.TableSpeedSearch
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.impl.ContentImpl
import com.intellij.ui.layout.panel
import com.intellij.ui.table.JBTable
import com.testbuddy.models.CoverageStatsObject
import com.testbuddy.services.CoverageDataService
import com.testbuddy.views.CoverageStatsCellRenderer
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

        val serv = project.service<CoverageDataService>()

        serv.getDiffLines(project)

        val content = panel {
        }
        content.isOpaque = false

        val vec = Vector<String>()
        vec.add("Name")
        vec.add("Coverage")
        vec.add("DiffButton")
        val table = JBTable(DefaultTableModel(vec, 0))

        table.columnModel.getColumn(1).cellRenderer = CoverageStatsCellRenderer()

        val model = table.model as DefaultTableModel

        for (x in serv.classCoveragesMap) {
            val vec = Vector<Any>()
            vec.add(x.key)

            if (x.value.allLines.isNotEmpty()) {
                val newLines = (x.value.coveredNow.size.toFloat() / x.value.allLines.size.toFloat() * 100).toInt()
                val oldLines = (x.value.coveredPrev.size.toFloat() / x.value.allLines.size.toFloat() * 100).toInt()
                val value = x.value

                vec.add(
                    CoverageStatsObject(
                        value.coveredNow.size,
                        value.allLines.size,
                        newLines,
                        newLines - oldLines
                    )
                )
            } else {
                vec.add("---")
            }

            vec.add("Diff")

            model.addRow(vec)
        }

        table.tableHeader.reorderingAllowed = false
        val speedSearch = TableSpeedSearch(table)
        speedSearch.setClearSearchOnNavigateNoMatch(true)
        table.autoCreateRowSorter = true
        table.rowSorter

        coverageViewport.view = table
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
