package com.testbuddy.actions

import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.ui.CheckedTreeNode
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.testingChecklist.leafNodes.CustomChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testbuddy.services.ChecklistTreeService
import com.testbuddy.services.TestMethodGenerationService
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class ModifyChecklistAction(private val node: CheckedTreeNode, private val project: Project) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {

        val service = project.service<ChecklistTreeService>()
        if (e.source is JBMenuItem) {
            if ((e.source as JBMenuItem).text == "Delete") deleteNode(service)
            if ((e.source as JBMenuItem).text == "Generate Test Method") generateTestMethod()
            if ((e.source as JBMenuItem).text == "Add item") addItem(service)
        }
    }

    /**
     * Add a new checklist item.
     *
     * @param service ChecklistTreeService which will add the item to the ChecklistTree
     */
    private fun addItem(service: ChecklistTreeService) {

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

    /**
     * Delete a node from the tree.
     *
     * @param service ChecklistTreeService which will delete the node from the ChecklistTree
     */
    private fun deleteNode(service: ChecklistTreeService) {
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
    }

    /**
     * This method just generate the test method for the selected item.
     *
     */
    private fun generateTestMethod() {
        val textEditor = (FileEditorManager.getInstance(project).selectedEditor as TextEditor?) ?: return
        val editor = textEditor.editor
        val generateMethod = project.service<TestMethodGenerationService>()

        generateMethod.generateTestMethod(
            project, editor,
            (node.userObject as ChecklistUserObject)
                .checklistNode as TestingChecklistLeafNode
        )
    }
}
