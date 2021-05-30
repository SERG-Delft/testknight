package com.testbuddy.views.actions

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.ui.CheckedTreeNode
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.testingChecklist.leafNodes.CustomChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testbuddy.services.ChecklistTreeService
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class ModifyChecklistAction(private val node: CheckedTreeNode, private val project: Project) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {

        val service = project.service<ChecklistTreeService>()

        // val node = path.lastPathComponent as CheckedTreeNode

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
        } else if (e.source is JBMenuItem && (e.source as JBMenuItem).text == "Edit") {
            println("Edit functionality")
        } else if (e.source is JBMenuItem && (e.source as JBMenuItem).text == "Add item") {

            val newItem = CustomChecklistNode("", null, 0)

            val descriptionMethod: String = (
                (node.userObject as ChecklistUserObject)
                    .checklistNode as TestingChecklistMethodNode
                ).description
            val elementMethod = (
                (node.userObject as ChecklistUserObject)
                    .checklistNode as TestingChecklistMethodNode
                ).element
            val listItems: MutableList<TestingChecklistLeafNode> = mutableListOf(newItem)

            val descriptionClass: String = (
                (
                    (node.parent as CheckedTreeNode)
                        .userObject as ChecklistUserObject
                    ).checklistNode as TestingChecklistClassNode
                ).description
            val elementClass = (
                (
                    (node.parent as CheckedTreeNode)
                        .userObject as ChecklistUserObject
                    ).checklistNode as TestingChecklistClassNode
                ).element

            val methodNode = TestingChecklistMethodNode(descriptionMethod, listItems, elementMethod)
            val listMethod: MutableList<TestingChecklistMethodNode> = mutableListOf(methodNode)
            val classNode = TestingChecklistClassNode(descriptionClass, listMethod, elementClass)
            service.addChecklist(classNode)
        }
    }
}
