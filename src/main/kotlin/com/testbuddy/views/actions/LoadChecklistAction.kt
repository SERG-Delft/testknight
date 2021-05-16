package com.testbuddy.views.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.impl.ContentImpl
import javax.swing.JTabbedPane
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class LoadChecklistAction : AnAction() {

    /**
     * Updates the CheckList tab to add new checklist cases.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(event: AnActionEvent) {

        // Return if the ToolWindow couldn't be found
        val window = ToolWindowManager.getInstance(event.project!!).getToolWindow("TestBuddy") ?: return

        val tabbedPane = (
            (window.contentManager.contents[0] as ContentImpl)
                .component as JTabbedPane
            )

        val copyPasteTab = tabbedPane.getComponentAt(1) as JBPanelWithEmptyText
        val copyPasteScroll = copyPasteTab.getComponent(1) as JBScrollPane
        val copyPasteViewport = copyPasteScroll.viewport
        val checklistTree = copyPasteViewport.view as CheckboxTree

        val checklistNode = DefaultMutableTreeNode("Checklist Method")
        checklistNode.add(CheckedTreeNode("Checklist item x"))
        (checklistTree.model.root as CheckedTreeNode).add(checklistNode)

        // Reload updates the UI to have the new nodes
        (checklistTree.model as DefaultTreeModel).reload()
    }

    /**
     * Determines whether this menu item is available for the current context.
     * Requires a project to be open and psiFile and Editor to be accessible from the action event.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    override fun update(e: AnActionEvent) {
        // Set the availability based on whether the project, psiFile and editor is not null
        e.presentation.isEnabled = (
            e.project != null &&
                e.getData(CommonDataKeys.PSI_FILE) != null &&
                e.getData(CommonDataKeys.EDITOR) != null
            )
    }
}
