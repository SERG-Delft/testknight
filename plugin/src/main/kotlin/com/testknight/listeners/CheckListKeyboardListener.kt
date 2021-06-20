package com.testknight.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.testknight.models.ChecklistUserObject
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testknight.services.ChecklistTreeService
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.tree.TreePath

class CheckListKeyboardListener(private val tree: CheckboxTree, private val project: Project) : KeyAdapter() {

    /**
     * Goes to the test method/class if ENTER has been pressed.
     * Duplicates the test method if SHIFT is also held along with pressing enter.
     * Delete a node if DELETE has been pressed
     *
     * @param e The key press event.
     */
    override fun keyPressed(e: KeyEvent) {
        if (e.keyCode == KeyEvent.VK_ENTER) {

            val path: TreePath = tree.selectionPath ?: return

            val node = path.lastPathComponent as CheckedTreeNode
            val userObject = ((node.userObject ?: return) as ChecklistUserObject)

            // If its the leaf node
            if (userObject.checklistNode is TestingChecklistLeafNode && node.isEnabled) {
                tree.setNodeState(node, !node.isChecked)
                e.consume()
            }
        } else if (e.keyCode == KeyEvent.VK_DELETE) {

            val path: TreePath = tree.selectionPath ?: return

            val node = path.lastPathComponent as CheckedTreeNode

            project.service<ChecklistTreeService>().deleteElement(node)
            e.consume()
        }
    }
}
