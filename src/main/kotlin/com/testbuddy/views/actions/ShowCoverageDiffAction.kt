package com.testbuddy.views.actions

import com.intellij.diff.tools.util.side.TwosideContentPanel
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.ui.WindowWrapper
import com.intellij.openapi.ui.WindowWrapperBuilder
import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.components.JBScrollPane
import com.testbuddy.services.CoverageHighlighterService

class ShowCoverageDiffAction : AnAction() {

    /**
     * Opens a new window which shows the diff view for coverage.
     *
     * @param e Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(e: AnActionEvent) {

        val editor = e.getData(CommonDataKeys.EDITOR)!!
        val vFile = e.getData(CommonDataKeys.VIRTUAL_FILE)!!
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        val className = PsiTreeUtil.findChildOfType(psiFile, PsiClass::class.java)?.name ?: return

        val editorFactory = EditorFactory.getInstance()

        val coverageHighlighterService = e.project!!.service<CoverageHighlighterService>()

        val leftEditor = editorFactory.createEditor(editor.document, null, vFile, true)
        val rightEditor = editorFactory.createEditor(editor.document, null, vFile, true)

        coverageHighlighterService.showHighlightsInDiff(leftEditor, rightEditor, className)

        val twoSidePanel = TwosideContentPanel(mutableListOf(leftEditor.component, rightEditor.component))

        val scrollPanel = JBScrollPane()
        scrollPanel.viewport.view = twoSidePanel
        val windowWrapper = WindowWrapperBuilder(WindowWrapper.Mode.FRAME, scrollPanel)
            .setProject(e.project)
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

    /**
     * Determines whether this menu item is available for the current context.
     * Requires a project to be open and PsiFile, VirtualFile and Editor to be accessible from the action event.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    override fun update(e: AnActionEvent) {
        // Set the availability based on whether the project, psiFile and editor is not null
        e.presentation.isEnabled = (
            e.project != null &&
                e.getData(CommonDataKeys.EDITOR) != null &&
                e.getData(CommonDataKeys.VIRTUAL_FILE) != null &&
                e.getData(CommonDataKeys.PSI_FILE) != null
            )
    }
}
