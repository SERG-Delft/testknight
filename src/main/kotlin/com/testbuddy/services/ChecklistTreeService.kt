package com.testbuddy.services

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.util.ui.tree.TreeUtil
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.TestingChecklist
import com.testbuddy.models.TestingChecklistClassNode
import com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.models.TestingChecklistMethodNode
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeNode

class ChecklistTreeService {
    lateinit var uiTree: CheckboxTree
    lateinit var dataTree: TestingChecklist

    fun initTrees(uiTree: CheckboxTree) {
        dataTree = TestingChecklist(mutableListOf())
        this.uiTree = uiTree
    }

    fun resetTree() {
        dataTree = TestingChecklist(mutableListOf())
        val root = uiTree.model.root as DefaultMutableTreeNode
        root.removeAllChildren()
    }

    private fun findElement(
        dataMethod: TestingChecklistMethodNode,
        itemNode: TestingChecklistLeafNode
    ): Boolean {

        var foundItem = false
        for (i in 0 until dataMethod.children.size) {

            val dataItem = dataMethod.children[i]
            // var uiTreeItem = uiTreeMethod.getChildAt(i)
            if (itemNode.description == dataItem.description &&
                itemNode.element == dataItem.element
            ) {
                foundItem = true
                break
                // i do not have to add this item
            }
        }
        return foundItem
    }

    private fun buildUiItem(
        foundItem: Boolean,
        uiTreeClassNode: TreeNode,
        uiTreeMethod: TreeNode,
        dataMethod: TestingChecklistMethodNode,
        itemNode: TestingChecklistLeafNode
    ) {
        if (foundItem == false && dataMethod.children.size != 0) {
            dataMethod.children.add(itemNode)

            val newItemNode = CheckedTreeNode(ChecklistUserObject(itemNode, 0))
            newItemNode.isChecked = false
            (uiTreeMethod as CheckedTreeNode).add(newItemNode)
            (uiTreeClassNode as CheckedTreeNode).add(uiTreeMethod)
        }
    }

    private fun findMethod(
        uiTreeClassNode: TreeNode,
        dataClass: TestingChecklistClassNode,
        methodNode: TestingChecklistMethodNode
    ): Boolean {
        var foundMethod: Boolean = false
        for (k in 0 until dataClass.children.size) {

            val dataMethod = dataClass.children[k]
            val uiTreeMethod = uiTreeClassNode.getChildAt(k)

            if (dataMethod.element == methodNode.element) {
                foundMethod = true
                dataMethod.description = methodNode.description
                for (itemNode in methodNode.children) {
                    val foundItem: Boolean = findElement(dataMethod, itemNode)
                    buildUiItem(foundItem, uiTreeClassNode, uiTreeMethod, dataMethod, itemNode)
                }
            }
        }
        return foundMethod
    }

    private fun buildUiClass(
        uiTreeClassNode: TreeNode,
        dataClass: TestingChecklistClassNode,
        methodNode: TestingChecklistMethodNode
    ) {
        dataClass.children.add(methodNode)
        val uiTreeMethod = CheckedTreeNode(ChecklistUserObject(methodNode, 0))
        for (item in methodNode.children) {
            val itemNode = CheckedTreeNode(ChecklistUserObject(item, 0))
            itemNode.isChecked = false
            uiTreeMethod.add(itemNode)
        }
        (uiTreeClassNode as CheckedTreeNode).add(uiTreeMethod)
    }

    private fun checkMethods(
        uiTreeClassNode: TreeNode,
        dataClass: TestingChecklistClassNode,
        newNode: TestingChecklistClassNode
    ) {
        for (methodNode in newNode.children) {
            // not finding the method, crate new method
            if (!findMethod(uiTreeClassNode, dataClass, methodNode)) {
                buildUiClass(uiTreeClassNode, dataClass, methodNode)
            }
        }
    }

    /**
     * This method will return true if it can find the class, false otherwise
     * @param uiTreeRoot CheckedTreeNode
     * @param newNode
     */
    private fun findClass(uiTreeRoot: CheckedTreeNode, newNode: TestingChecklistClassNode): Boolean {

        var foundClass = false

        for (l in 0 until dataTree.classChecklists.size) {
            val dataClass = dataTree.classChecklists[l]
            val uiTreeClassNode = uiTreeRoot.getChildAt(l)
            if (dataClass.element == newNode.element) {
                foundClass = true
                dataClass.description = newNode.description
                checkMethods(uiTreeClassNode, dataClass, newNode)
            }
            break
        }
        return foundClass
    }

    /**
     * Adds a new TestingChecklistClassNode if it was not there before
     * @param newNode TestingChecklistClassNode which represents the new node which have to be added
     */
    fun addChecklist(newNode: TestingChecklistClassNode) {

        val uiTreeRoot = uiTree.model.root as CheckedTreeNode

        // not finding the method, create new classs
        if (!findClass(uiTreeRoot, newNode)) {
            dataTree.classChecklists.add(newNode)
            val classNode = CheckedTreeNode(ChecklistUserObject(newNode, 0))
            uiTreeRoot.add(classNode)
            for (method in newNode.children) {

                val methodNode = CheckedTreeNode(ChecklistUserObject(method, 0))

                for (item in method.children) {
                    val itemNode = CheckedTreeNode(ChecklistUserObject(item, 0))
                    itemNode.isChecked = false
                    methodNode.add(itemNode)
                }
                classNode.add(methodNode)
            }
        }
        (uiTree.model as DefaultTreeModel).reload()
        TreeUtil.expandAll(uiTree)
    }

    /**
     * This function print the tree on the terminal. It is very useful for debugging
     */
    fun print() {
        println(" am here")
        println(dataTree.classChecklists.size)
        for (dataClass in dataTree.classChecklists) {
            //  println(" a2m here")
            for (dataMethod in dataClass.children) {
                for (dataItem in dataMethod.children) {
                    println(dataClass.description + " " + dataMethod.description + " " + dataItem.description)
                }
            }
        }
    }
}
