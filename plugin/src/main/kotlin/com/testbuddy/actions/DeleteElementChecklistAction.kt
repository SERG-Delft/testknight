package com.testbuddy.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.openapi.ui.JBPopupMenu
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.services.ChecklistTreeService
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

class DeleteElementChecklistAction : AnAction()
{

    private lateinit var tree: Tree


    override fun actionPerformed(e: AnActionEvent) {

        val path: TreePath = tree.selectionPath ?: return

        if (path.lastPathComponent !is DefaultMutableTreeNode) return

        val node = path.lastPathComponent as CheckedTreeNode

        e.project?.service<ChecklistTreeService>()?.deleteElement(node) ?:return
    }

    /**
     * Getter for the tree attribute
     *
     * @return the Tree attribute
     */
    fun getTree(): Tree {
      return tree
    }

    /**
     * Setter for the tree attribute
     *
     * @param tree the new value of the Tree attribute
     */
    fun setTree(tree: Tree) {
        this.tree = tree
    }

}