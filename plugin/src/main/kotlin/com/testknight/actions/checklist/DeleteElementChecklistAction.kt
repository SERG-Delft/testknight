package com.testknight.actions.checklist

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.treeStructure.Tree
import com.testknight.services.ExceptionHandlerService
import com.testknight.services.checklist.ChecklistTreeService
import javax.swing.tree.TreePath

class DeleteElementChecklistAction : AnAction() {

    private lateinit var tree: Tree

    override fun actionPerformed(e: AnActionEvent) {

        val path: TreePath = tree.selectionPath

        if (path.lastPathComponent !is CheckedTreeNode) {
            notifyUser(e)
            return
        }

        val node = path.lastPathComponent as CheckedTreeNode
        if (e.project == null) {
            return
        } else {
            e.project!!.service<ChecklistTreeService>().deleteElement(node)
        }
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        if (tree.selectionPath == null) {
            e.presentation.isEnabled = false
        }
    }

    /**
     * Getter for the tree attribute.
     *
     * @return the Tree attribute
     */
    fun getTree(): Tree {
        return tree
    }

    /**
     * Setter for the tree attribute.
     *
     * @param tree the new value of the Tree attribute
     */
    fun setTree(tree: Tree) {
        this.tree = tree
    }

    /**
     * Notify the user in case of no path found.
     *
     * @param e the AnActionEvent for which the user must be notified
     */
    private fun notifyUser(e: AnActionEvent) {
        if (e.project == null) {
            return
        } else {
            e.project!!.service<ExceptionHandlerService>().notify(
                "Delete Element not available",
                "Not selected element", NotificationType.WARNING
            )
        }
    }
}
