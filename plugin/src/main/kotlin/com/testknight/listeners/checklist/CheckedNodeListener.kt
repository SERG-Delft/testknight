package com.testknight.listeners.checklist

import com.intellij.ui.CheckboxTreeListener
import com.intellij.ui.CheckedTreeNode
import com.testknight.models.ChecklistUserObject
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testknight.services.UsageDataService

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
                    userObject.checklistNode.checked = 1
                    val parent = (node.parent as CheckedTreeNode)
                    (parent.userObject as ChecklistUserObject).checklistNode.checked += 1

                    val grandParent = (parent.parent as CheckedTreeNode)
                    (grandParent.userObject as ChecklistUserObject).checklistNode.checked += 1
                    UsageDataService.instance.recordItemMarked()
                } else {
                    userObject.checklistNode.checked = 0
                    val parent = (node.parent as CheckedTreeNode)
                    (parent.userObject as ChecklistUserObject).checklistNode.checked -= 1

                    val grandParent = (parent.parent as CheckedTreeNode)
                    (grandParent.userObject as ChecklistUserObject).checklistNode.checked -= 1
                }
            }
        }
    }
}
