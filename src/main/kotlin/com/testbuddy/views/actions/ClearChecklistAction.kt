package com.testbuddy.views.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.utilities.UserInterfaceHelper
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class ClearChecklistAction : AnAction() {

    /**
     * Clears the Checklist tree.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project!!
        val viewport = UserInterfaceHelper.getTabViewport(project, "Checklist") ?: return
        val checklistTree = viewport.view as Tree
        val root = checklistTree.model.root as DefaultMutableTreeNode
        root.removeAllChildren()
        (checklistTree.model as DefaultTreeModel).reload()
    }

    /**
     * Determines whether this action button is available for the current context.
     * Requires a project to be open.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    override fun update(e: AnActionEvent) {
        // Set the availability if project is open
        e.presentation.isEnabled = e.project != null
    }
}
