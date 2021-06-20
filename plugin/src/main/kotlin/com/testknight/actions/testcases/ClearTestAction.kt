package com.testknight.actions.testcases

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.ui.treeStructure.Tree
import com.testknight.utilities.UserInterfaceHelper
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class ClearTestAction : AnAction() {

    /**
     * Clears the TestList tree.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(event: AnActionEvent) {
        actionPerformed(event.project!!)
    }

    /**
     * Clears the TestList tree.
     *
     * @param project current open project.
     */
    fun actionPerformed(project: Project) {
        val viewport = UserInterfaceHelper.getTabViewport(project, "Test List") ?: return
        val testListTree = viewport.view as Tree
        val root = testListTree.model.root as DefaultMutableTreeNode
        root.removeAllChildren()
        (testListTree.model as DefaultTreeModel).reload()
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
