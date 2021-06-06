package com.testbuddy.actions

import com.intellij.diff.tools.util.side.TwosideContentPanel
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.WindowWrapper
import com.intellij.openapi.ui.WindowWrapperBuilder
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import com.testbuddy.exceptions.DocumentNotFoundException
import com.testbuddy.exceptions.InvalidVirtualFileException
import com.testbuddy.services.CoverageDataService
import com.testbuddy.services.CoverageHighlighterService
import com.testbuddy.services.UsageDataService
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ShowCoverageDiffAction(val table: JBTable, val project: Project) : AbstractAction() {

    /**
     * Opens a new window which shows the diff view for coverage.
     *
     * @param e Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(e: ActionEvent?) {
        e ?: return // return if event is null

        val row = e.actionCommand.toInt()
        val className = table.model.getValueAt(row, 0) as String

        val serv = project.service<CoverageDataService>()

        if (serv.classCoveragesMap[className] == null) {
            throw InvalidVirtualFileException(className, "Class not found in coverage.")
        }

        val vFile = serv.classCoveragesMap[className]!!.virtualFile
            ?: throw InvalidVirtualFileException(className, "Virtual file is null.")

        if (!vFile.isValid) {
            throw InvalidVirtualFileException(
                className,
                "File (or parent) deleted or external change."
            )
        }

        val covObj = serv.classCoveragesMap[className]!!

        if (vFile.modificationStamp != covObj.currStamp || covObj.prevStamp != covObj.currStamp) {
            return
        }

        val document = FileDocumentManager.getInstance().getDocument(vFile)
            ?: throw DocumentNotFoundException()

        val editorFactory = EditorFactory.getInstance()

        val coverageHighlighterService = project.service<CoverageHighlighterService>()

        val leftEditor = editorFactory.createEditor(document, null, vFile, true)
        val rightEditor = editorFactory.createEditor(document, null, vFile, true)

        coverageHighlighterService.showHighlightsInDiff(leftEditor, rightEditor, className)

        val twoSidePanel = TwosideContentPanel(mutableListOf(leftEditor.component, rightEditor.component))

        val scrollPanel = JBScrollPane()
        scrollPanel.viewport.view = twoSidePanel
        val windowWrapper = WindowWrapperBuilder(WindowWrapper.Mode.FRAME, scrollPanel)
            .setProject(project)
            .setTitle("Diff Coverage")
            // releaseEditor on close.
            .setOnCloseHandler {
                editorFactory.releaseEditor(leftEditor)
                editorFactory.releaseEditor(rightEditor)
                true // requires a boolean return, true after releasing editor
            }
            .build()

        windowWrapper.show()
        UsageDataService.instance.logSplitDiffView()
    }
}
