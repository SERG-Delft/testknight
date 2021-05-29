package com.testbuddy.views.listeners

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.models.ChecklistUserObject
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.tree.TreePath

class CheckListKeyboardListener(private val tree: CheckboxTree) : KeyAdapter() {

    /**
     * Goes to the test method/class if ENTER has been pressed.
     * Duplicates the test method if SHIFT is also held along with pressing enter.
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
        }
    }
}
