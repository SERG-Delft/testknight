package com.testbuddy.actions

import com.intellij.diff.tools.util.side.TwosideContentPanel
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.WindowWrapper
import com.intellij.openapi.ui.WindowWrapperBuilder
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import com.testbuddy.services.CoverageDataService
import com.testbuddy.services.CoverageHighlighterService
import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class ShowCoverageDiffAction(val table: JBTable, val project: Project) : AbstractAction() {

    override fun actionPerformed(p0: ActionEvent?) {
        if (p0 != null) {
            val row = p0.actionCommand.toInt()
            val className = table.model.getValueAt(row, 0) as String
            val psiFiles = FilenameIndex.getFilesByName(
                project,
                "$className.java",
                GlobalSearchScope.projectScope(project)
            )

            val serv = project.service<CoverageDataService>()
            if (serv.currentData?.classes?.contains(className) ?: return) {
                serv.currentData!!.classes[className]
            }

            // Couldn't find the file.
            if (psiFiles.isEmpty()) {
                return
            }

            val vFile = psiFiles[0].virtualFile
            val document = PsiDocumentManager.getInstance(project).getDocument(psiFiles[0]) ?: return

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
        }
    }
}
