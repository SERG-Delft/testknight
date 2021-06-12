package com.testbuddy.actions

import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.testingChecklist.leafNodes.CustomChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testbuddy.services.ChecklistTreeService
import com.testbuddy.services.ExceptionHandlerService
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

class AddItemChecklistAction : AnAction() {

    private lateinit var tree: Tree

    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    override fun actionPerformed(e: AnActionEvent) {

        val path: TreePath = tree.selectionPath

        if (path.lastPathComponent !is CheckedTreeNode) {
            notifyUser(e)
        }

        val node = path.lastPathComponent as CheckedTreeNode
        if ((node.userObject as ChecklistUserObject).checklistNode is TestingChecklistMethodNode) {
            addItem(e, node, path)
            return
        }
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        if (tree.selectionPath == null) {
            e.presentation.isEnabled = false
            return
        }
        val path: TreePath = tree.selectionPath

        if (path.lastPathComponent !is DefaultMutableTreeNode) {
            e.presentation.isEnabled = false
            return
        }

        val node = path.lastPathComponent as CheckedTreeNode
        if ((node.userObject as ChecklistUserObject).checklistNode !is TestingChecklistMethodNode) {
            e.presentation.isEnabled = false
            return
        }
    }

    /**
     * Add a new checklist item.
     *
     * @param e the AnActionEvent which triggers the adding a new item.
     * @param node the CheckedTreeNode which have to be added.
     * @param path the TreePath which represents the path for which a node will be added.
     *
     */
    private fun addItem(e: AnActionEvent, node: CheckedTreeNode, path: TreePath) {

        val project = e.project
        val service = project?.service<ChecklistTreeService>()

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
        service?.addChecklist(classNode)

        // It always edit the last child as that would be the newly added node.
        val newChild = tree.model.getChild(node, tree.model.getChildCount(node) - 1)
        val selectionPath = path.pathByAddingChild(newChild)
        tree.startEditingAtPath(selectionPath)
    }

    /**
     * Getter for the tree attribute.
     *
     * @return the Tree attribute
     */
    fun getTree(): Tree {
        return tree
    }

    /**
     * Setter for the tree attribute.
     *
     * @param tree the new value of the Tree attribute
     */
    fun setTree(tree: Tree) {
        this.tree = tree
    }

    /**
     * Notify the user in case that the selected node is not a DefaultMutableTreeNode.
     *
     * @param e the AnActionEvent for which the user must be notified
     */
    private fun notifyUser(e: AnActionEvent) {
        e.project?.service<ExceptionHandlerService>()?.notify(
            "Add item not available",
            "The method for the item is not selected", NotificationType.WARNING
        ) ?: return
    }
}
