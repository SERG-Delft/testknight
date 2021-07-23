package com.testknight.actions.checklist

import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.treeStructure.Tree
import com.testknight.models.ChecklistUserObject
import com.testknight.models.testingChecklist.leafNodes.CustomChecklistNode
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testknight.services.checklist.ChecklistTreeService
import com.testknight.services.TestMethodGenerationService
import com.testknight.services.UsageDataService
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.tree.TreePath

class ModifyChecklistAction(
    private val node: CheckedTreeNode,
    private val tree: Tree,
    private val path: TreePath,
    private val project: Project
) : ActionListener {

    override fun actionPerformed(e: ActionEvent) {

        if (e.source is JBMenuItem) {
            if ((e.source as JBMenuItem).text == "Delete") {
                project.service<ChecklistTreeService>().deleteElement(node)
            }
            if ((e.source as JBMenuItem).text == "Generate Test Method") generateTestMethod()
            if ((e.source as JBMenuItem).text == "Add item") addItem(tree, path)
        }
    }

    /**
     * Add a new checklist item.
     *
     */
    private fun addItem(tree: Tree, path: TreePath) {

        val service = project.service<ChecklistTreeService>()

        val newItem = CustomChecklistNode("", null, 0)
        val testingChecklistMethodNode = (node.userObject as ChecklistUserObject)
            .checklistNode as TestingChecklistMethodNode
        val testingChecklistClassNode = (
            (node.parent as CheckedTreeNode)
                .userObject as ChecklistUserObject
            ).checklistNode as TestingChecklistClassNode

        val descriptionMethod: String = testingChecklistMethodNode.description
        val elementMethod = testingChecklistMethodNode.element
        val listItems: MutableList<TestingChecklistLeafNode> = mutableListOf(newItem)

        val descriptionClass: String = testingChecklistClassNode.description
        val elementClass = testingChecklistClassNode.element

        val methodNode = TestingChecklistMethodNode(descriptionMethod, listItems, elementMethod)
        val listMethod: MutableList<TestingChecklistMethodNode> = mutableListOf(methodNode)
        val classNode = TestingChecklistClassNode(descriptionClass, listMethod, elementClass)
        service.addChecklist(classNode)

        val newChild = tree.model.getChild(node, tree.model.getChildCount(node) - 1)
        val selectionPath = path.pathByAddingChild(newChild)
        tree.startEditingAtPath(selectionPath)
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
            editor,
            (node.userObject as ChecklistUserObject)
                .checklistNode as TestingChecklistLeafNode
        )
        UsageDataService.instance.recordGenerateTest()
    }
}
