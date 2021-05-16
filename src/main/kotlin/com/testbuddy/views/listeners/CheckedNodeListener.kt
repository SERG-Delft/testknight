package com.testbuddy.views.listeners

import com.intellij.ui.CheckboxTreeListener
import com.intellij.ui.CheckedTreeNode
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.TestingChecklistLeafNode

/**
* Custom CheckboxTreelistener which support counting the selected items.
*
* @param checklistTree CheckboxTree the listener listens to.
*/
class CheckedNodeListener : CheckboxTreeListener {

    override fun nodeStateChanged(node: CheckedTreeNode) {

        if (node.userObject is ChecklistUserObject) {
            val userObject = node.userObject as ChecklistUserObject

            if (userObject.checklistNode is TestingChecklistLeafNode) {
                if (node.isChecked) {
                    val parent = (node.parent as CheckedTreeNode)
                    (parent.userObject as ChecklistUserObject).checkCount += 1

                    val grandParent = (parent.parent as CheckedTreeNode)
                    (grandParent.userObject as ChecklistUserObject).checkCount += 1
                } else {
                    val parent = (node.parent as CheckedTreeNode)
                    (parent.userObject as ChecklistUserObject).checkCount -= 1

                    val grandParent = (parent.parent as CheckedTreeNode)
                    (grandParent.userObject as ChecklistUserObject).checkCount -= 1
                }
            }
        }
    }
}
