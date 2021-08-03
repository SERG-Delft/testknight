package com.testknight.actions.checklist

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.treeStructure.Tree
import com.testknight.models.ChecklistUserObject
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testknight.services.ExceptionHandlerService
import javax.swing.tree.TreePath

class EditItemChecklistAction : AnAction() {
    private lateinit var tree: Tree

    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent) {

        val path: TreePath = tree.selectionPath

        if (path.lastPathComponent !is CheckedTreeNode) {
            notifyUser(e)
        }

        val node = path.lastPathComponent as CheckedTreeNode
        if ((node.userObject as ChecklistUserObject).checklistNode is TestingChecklistLeafNode) {
            tree.startEditingAtPath(path)
            return
        }
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        if (tree.selectionPath == null) {
            e.presentation.isEnabled = false
            return
        }
        val path: TreePath = tree.selectionPath

        if (path.lastPathComponent !is CheckedTreeNode) {
            e.presentation.isEnabled = false
            return
        }

        val node = path.lastPathComponent as CheckedTreeNode
        if ((node.userObject as ChecklistUserObject).checklistNode !is TestingChecklistLeafNode) {
            e.presentation.isEnabled = false
            return
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
     * Notify the user in case that something goes wrong.
     *
     * @param e the AnActionEvent for which the user must be notified
     */
    private fun notifyUser(e: AnActionEvent) {
        if (e.project == null) {
            return
        } else {
            e.project!!.service<ExceptionHandlerService>().notify(
                "Edit item not available",
                "The item for edit not selected", NotificationType.WARNING
            )
        }
    }
}
