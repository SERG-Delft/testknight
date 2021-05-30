package com.testbuddy.views.listeners

import com.intellij.ui.CheckboxTreeListener
import com.intellij.ui.CheckedTreeNode
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

/**
 * Custom CheckboxTreeListener which support counting the selected items.
 *
 * @param checklistTree CheckboxTree the listener listens to.
 */
class CheckedNodeListener : CheckboxTreeListener {

    /**
     * This method just makes the changes for the number of checked nodes for the selected node.
     *
     * @param node the selected CheckedTreeNode for which we have to do the changes in the checked attribute
     */
    override fun nodeStateChanged(node: CheckedTreeNode) {

        if (node.userObject is ChecklistUserObject) {
            val userObject = node.userObject as ChecklistUserObject

            if (userObject.checklistNode is TestingChecklistLeafNode) {
                if (node.isChecked) {
                    val parent = (node.parent as CheckedTreeNode)
                    (parent.userObject as ChecklistUserObject).checklistNode.checked += 1

                    val grandParent = (parent.parent as CheckedTreeNode)
                    (grandParent.userObject as ChecklistUserObject).checklistNode.checked += 1
                } else {
                    val parent = (node.parent as CheckedTreeNode)
                    (parent.userObject as ChecklistUserObject).checklistNode.checked -= 1

                    val grandParent = (parent.parent as CheckedTreeNode)
                    (grandParent.userObject as ChecklistUserObject).checklistNode.checked -= 1
                }
            }
        }
    }
}
