package com.testbuddy.views.listeners

import com.intellij.ui.CheckboxTreeListener
import com.intellij.ui.CheckedTreeNode
import com.testbuddy.models.ChecklistNode

/**
* Custom CheckboxTreelistener which support counting the selected items.
*
* @param checklistTree CheckboxTree the listener listens to.
*/
class CheckedNodeListener : CheckboxTreeListener {

    override fun nodeStateChanged(node: CheckedTreeNode) {

        if (node.userObject is ChecklistNode) {
            val userObject = node.userObject as ChecklistNode

            if (userObject.depth == 1) {
                if (node.isChecked) {
                    val parent = (node.parent as CheckedTreeNode)
                    (parent.userObject as ChecklistNode).checkCount += 1

                    val grandParent = (parent.parent as CheckedTreeNode)
                    (grandParent.userObject as ChecklistNode).checkCount += 1
                } else {
                    val parent = (node.parent as CheckedTreeNode)
                    (parent.userObject as ChecklistNode).checkCount -= 1

                    val grandParent = (parent.parent as CheckedTreeNode)
                    (grandParent.userObject as ChecklistNode).checkCount -= 1
                }
            }
        }
    }
}
