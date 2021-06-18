package com.testknight.actions

import com.intellij.diff.tools.util.side.TwosideContentPanel
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.WindowWrapper
import com.intellij.openapi.ui.WindowWrapperBuilder
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import com.testknight.exceptions.DocumentNotFoundException
import com.testknight.exceptions.InvalidVirtualFileException
import com.testknight.services.CoverageDataService
import com.testknight.services.CoverageHighlighterService
import com.testknight.services.ExceptionHandlerService
import com.testknight.services.UsageDataService
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

        // Return if diff is not available.
        if (!serv.isDiffAvailable(className, vFile.modificationStamp)) {
            project.service<ExceptionHandlerService>()
                .notify(
                    "Source File Has Changed.",
                    "The source file for the selected class has" +
                        " either been modified since the latest run with coverage\n" +
                        "or doesn't have the same source code since the past two runs.",
                    NotificationType.WARNING
                )

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
        UsageDataService.instance.recordSplitDiffView()
    }
}
