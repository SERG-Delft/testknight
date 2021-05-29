package com.testbuddy.views.actions

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.ui.CheckedTreeNode
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testbuddy.com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.services.ChecklistTreeService
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class DeleteChecklistAction(private val node: CheckedTreeNode, private val project: Project) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {

        val service = project.service<ChecklistTreeService>()

        if (e.source is JBMenuItem && (e.source as JBMenuItem).text == "Delete") {

            if ((node.userObject as ChecklistUserObject).checklistNode is TestingChecklistClassNode) {

                service.deleteClass(
                    (node.userObject as ChecklistUserObject)
                        .checklistNode as TestingChecklistClassNode
                )
            } else if ((node.userObject as ChecklistUserObject)
                .checklistNode is TestingChecklistMethodNode
            ) {

                service.deleteMethod(
                    (node.userObject as ChecklistUserObject)
                        .checklistNode as TestingChecklistMethodNode,
                    ((node.parent as CheckedTreeNode).userObject as ChecklistUserObject)
                        .checklistNode as TestingChecklistClassNode
                )
            } else if ((node.userObject as ChecklistUserObject)
                .checklistNode is TestingChecklistLeafNode
            ) {

                service.deleteItem(
                    (node.userObject as ChecklistUserObject).checklistNode as TestingChecklistLeafNode,
                    ((node.parent as CheckedTreeNode).userObject as ChecklistUserObject)
                        .checklistNode as TestingChecklistMethodNode,
                    ((node.parent.parent as CheckedTreeNode).userObject as ChecklistUserObject)
                        .checklistNode as TestingChecklistClassNode
                )
            }
        } else if (e.source is JBMenuItem && (e.source as JBMenuItem).text == "edit") {
            println("Edit functionality")
        }
    }
}
