package com.testbuddy.views.listeners

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.openapi.ui.JBPopupMenu
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.views.actions.DeleteChecklistAction
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

class ChecklistMouseListener(private val tree: Tree, private val project: Project) : MouseAdapter() {

    override fun mousePressed(event: MouseEvent) {

        if (SwingUtilities.isRightMouseButton(event)) {

            val path: TreePath = tree.selectionPath ?: return

            if (path.lastPathComponent !is DefaultMutableTreeNode) return

            val node = path.lastPathComponent as CheckedTreeNode

            val menu = JBPopupMenu()
            val delete = JBMenuItem("Delete")
            delete.addActionListener(DeleteChecklistAction(node, project))
            menu.add(delete)

            if ((node.userObject as ChecklistUserObject).checklistNode is TestingChecklistLeafNode) {
                val edit = JBMenuItem("Edit")
                edit.addActionListener(DeleteChecklistAction(node, project))
                menu.add(edit)
            }

            menu.show(tree, event.x, event.y)

            // If the node contains the userObject which is expected from a test method node.
            println(node.userObject)
            println(node.parent)
        }
    }
}
