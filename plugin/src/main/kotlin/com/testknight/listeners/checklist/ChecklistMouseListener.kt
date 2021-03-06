package com.testknight.listeners.checklist

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.openapi.ui.JBPopupMenu
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.treeStructure.Tree
import com.testknight.actions.checklist.ModifyChecklistAction
import com.testknight.models.ChecklistUserObject
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistMethodNode
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
            delete.addActionListener(ModifyChecklistAction(node, tree, path, project))
            menu.add(delete)

            if ((node.userObject as ChecklistUserObject).checklistNode is TestingChecklistLeafNode) {
                val edit = JBMenuItem("Edit")
                val generate = JBMenuItem("Generate Test Method")

                generate.addActionListener(ModifyChecklistAction(node, tree, path, project))

                edit.addActionListener {
                    it.apply { tree.startEditingAtPath(path) }
                }

                menu.add(edit)
                menu.add(generate)
            } else if ((node.userObject as ChecklistUserObject).checklistNode is TestingChecklistMethodNode) {
                val addItem = JBMenuItem("Add item")
                addItem.addActionListener(ModifyChecklistAction(node, tree, path, project))
                menu.add(addItem)
            }

            menu.show(tree, event.x, event.y)
        }
    }
}
