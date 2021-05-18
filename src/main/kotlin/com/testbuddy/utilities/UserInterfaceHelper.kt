package com.testbuddy.utilities

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiManager
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.impl.ContentImpl
import com.testbuddy.views.actions.testcases.ClearTestAction
import com.testbuddy.views.actions.testcases.LoadTestAction
import javax.swing.JTabbedPane
import javax.swing.JViewport

class UserInterfaceHelper private constructor() {
    companion object {
        /**
         * Updates the CopyPasteTab by calling the LoadTestAction.
         * Uses the project to get editor and psi file information.
         *
         * @param project the current project.
         */
        fun refreshTestCaseUI(project: Project) {
            val textEditor = FileEditorManager.getInstance(project).selectedEditor as TextEditor?

            val actionManager = ActionManager.getInstance()
            val clearTestAction =
                (actionManager.getAction("ClearTestAction") ?: return) as ClearTestAction

            if (textEditor != null) {
                val psiFile = PsiManager.getInstance(project).findFile(textEditor.file!!)

                if (psiFile == null) {
                    clearTestAction.actionPerformed(project)
                } else {
                    val loadTestAction =
                        (actionManager.getAction("LoadTestAction") ?: return) as LoadTestAction

                    loadTestAction.actionPerformed(project, psiFile, textEditor.editor)
                }
            } else {
                clearTestAction.actionPerformed(project)
            }
        }

        /**
         * Gets the Tab in the TestBuddy tool window with the given tab name.
         *
         * @param project Current open project.
         * @param tabName Name of the tab which needs to be returned.
         * @return The first tab with the name mentioned in tabName.
         *         If no such tab is found, returns null.
         */
        private fun getTab(project: Project, tabName: String): JBPanelWithEmptyText? {

            val window = ToolWindowManager.getInstance(project).getToolWindow("TestBuddy") ?: return null

            val tabbedPane = (
                (window.contentManager.contents[0] as ContentImpl)
                    .component as JTabbedPane
                )
            val tabIndex = tabbedPane.indexOfTab(tabName)
            if (tabIndex == -1) return null
            return tabbedPane.getComponentAt(tabIndex) as JBPanelWithEmptyText
        }

        /**
         * Gets the viewport of the tab in the TestBuddy tool window with the given tab name.
         *
         * @param project Current open project.
         * @param tabName Name of the tab which needs to be returned.
         * @return The viewport of the first tab with the name mentioned in tabName.
         *         If no such tab is found, returns null.
         */
        fun getTabViewport(project: Project, tabName: String): JViewport? {
            val tab = getTab(project, tabName) ?: return null
            val tabScrollPane = tab.getComponent(1) as JBScrollPane
            return tabScrollPane.viewport
        }
    }
}
