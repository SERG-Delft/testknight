package com.testknight.services.checklist

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.util.ui.tree.TreeUtil
import com.testknight.models.ChecklistUserObject
import com.testknight.models.testingChecklist.TestingChecklist
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testknight.services.UsageDataService
import com.testknight.views.trees.ChecklistCellRenderer
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreeNode

@Suppress("TooManyFunctions")
class ChecklistTreeService(val project: Project) {

    private lateinit var uiTree: CheckboxTree
    private lateinit var dataTree: TestingChecklist

    /**
     * This method initialize the trees, it acts like a constructor.
     *
     * @param uiTree the CheckboxTree which have to be initialized
     */
    fun initTrees(uiTree: CheckboxTree) {
        dataTree = project.service<ChecklistTreePersistent>().state
        this.uiTree = uiTree
        resetTree()
    }

    /**
     * Getter for the UI tree.
     *
     * @return the UI tree
     */
    fun getUiTree(): CheckboxTree {
        return uiTree
    }

    /**
     * Init the UI tree from the file.
     */
    fun initUiTree() {

        dataTree = project.service<ChecklistTreePersistent>().state
        val root = CheckedTreeNode("root")
        uiTree = CheckboxTree(ChecklistCellRenderer(true), root)
        val uiTreeRoot = uiTree.model.root as CheckedTreeNode

        for (classNode in dataTree.classChecklists) {
            val uiTreeClassNode = CheckedTreeNode(ChecklistUserObject(classNode))
            for (methodNode in classNode.children) {
                val uiTreeMethod = CheckedTreeNode(ChecklistUserObject(methodNode))
                for (item in methodNode.children) {
                    val itemNode = CheckedTreeNode(ChecklistUserObject(item))
                    itemNode.isChecked = (item.checked > 0)
                    uiTreeMethod.add(itemNode)
                }
                uiTreeClassNode.add(uiTreeMethod)
            }
            uiTreeRoot.add(uiTreeClassNode)
        }
        (uiTree.model as DefaultTreeModel).reload()
        TreeUtil.expandAll(uiTree)
    }

    /**
     * This method resets the tree and all the previous information is lost.
     */
    fun resetTree() {
        dataTree.classChecklists.clear()
        val root = uiTree.model.root as DefaultMutableTreeNode
        root.removeAllChildren()
        (uiTree.model as DefaultTreeModel).reload()
    }

    /**
     * This method returns true if it finds the element in the given method, false otherwise.
     *
     * @param dataMethod the TestingChecklistMethodNode for which we have to check if the element exists
     * @param itemNode the TestingChecklistLeafNode which we have to find
     */
    private fun findElement(
        dataMethod: TestingChecklistMethodNode,
        itemNode: TestingChecklistLeafNode
    ): Boolean {
        var foundItem = false

        for (i in 0 until dataMethod.children.size) {
            val dataItem = dataMethod.children[i]

            if (itemNode.description == dataItem.description) {
                foundItem = true
                break
                // i do not have to add this item
            }
        }
        return foundItem
    }

    /**
     * This method builds the UI for a checklist item
     *
     * @param foundItem the Boolean which represents if the item was found or not
     * @param uiTreeMethod: the TreeNode which represents the method reference of the UI
     * @param dataMethod the TestingChecklistMethodNode which represents the method of the tree
     * @param itemNode the TestingChecklistLeafNode which we have to be append to the tree
     */
    private fun buildUiItem(
        foundItem: Boolean,
        uiTreeMethod: TreeNode,
        dataMethod: TestingChecklistMethodNode,
        itemNode: TestingChecklistLeafNode
    ) {
        if (!foundItem) {
            dataMethod.children.add(itemNode)

            val newItemNode = CheckedTreeNode(ChecklistUserObject(itemNode))
            newItemNode.isChecked = false
            (uiTreeMethod as CheckedTreeNode).add(newItemNode)
        }
    }

    /**
     * This method returns true if it finds the method in the given class, false otherwise.
     *
     * @param uiTreeClassNode: the TreeNode which represents the reference of the UI
     * @param dataClass the TestingChecklistClassNode for which we have to check if the method exists
     * @param methodNode the TestingChecklistMethodNode which we have to find
     */
    private fun findMethod(
        uiTreeClassNode: TreeNode,
        dataClass: TestingChecklistClassNode,
        methodNode: TestingChecklistMethodNode
    ): Boolean {
        var foundMethod: Boolean = false
        for (k in 0 until dataClass.children.size) {

            val dataMethod = dataClass.children[k]
            val uiTreeMethod = uiTreeClassNode.getChildAt(k)

            if (dataMethod.description == methodNode.description) {
                foundMethod = true

                for (itemNode in methodNode.children) {
                    val foundItem: Boolean = findElement(dataMethod, itemNode)
                    buildUiItem(foundItem, uiTreeMethod, dataMethod, itemNode)
                }
            }
        }
        return foundMethod
    }

    /**
     * This method builds the UI for a checklist class
     *
     * @param uiTreeClassNode: the TreeNode which represents the class reference of the UI
     * @param dataClass the TestingChecklistClassNode which represents the class of the tree
     * @param methodNode the TestingChecklistMethodNode which we have to be append to the tree
     */
    private fun buildUiClass(
        uiTreeClassNode: TreeNode,
        dataClass: TestingChecklistClassNode,
        methodNode: TestingChecklistMethodNode
    ) {
        dataClass.children.add(methodNode)
        val uiTreeMethod = CheckedTreeNode(ChecklistUserObject(methodNode))
        for (item in methodNode.children) {
            val itemNode = CheckedTreeNode(ChecklistUserObject(item))
            itemNode.isChecked = false
            uiTreeMethod.add(itemNode)
        }
        (uiTreeClassNode as CheckedTreeNode).add(uiTreeMethod)
    }

    /**
     * This method builds the UI for the checklist methods of a checklist class
     *
     * @param uiTreeClassNode: the TreeNode which represents the class reference of the UI
     * @param dataClass the TestingChecklistClassNode which represents the class of the tree
     * @param newNode the TestingChecklistClassNode which we have to be append to the tree
     */
    private fun buildUiMethods(
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
     * This method returns true if it finds the class in the local tree, false otherwise.
     *
     * @param uiTreeRoot the CheckedTreeNode which represents the reference of the UI root tree
     * @param newNode the TestingChecklistClassNode which we have to be find it in the local tree
     */
    private fun findClass(uiTreeRoot: CheckedTreeNode, newNode: TestingChecklistClassNode): Boolean {

        var foundClass = false

        for (l in 0 until dataTree.classChecklists.size) {
            val dataClass = dataTree.classChecklists[l]
            val uiTreeClassNode = uiTreeRoot.getChildAt(l)
            if (dataClass.description == newNode.description) {
                foundClass = true
                buildUiMethods(uiTreeClassNode, dataClass, newNode)
                break
            }
        }
        return foundClass
    }

    /**
     * Adds a new TestingChecklistClassNode if it was not there before
     * @param newNode TestingChecklistClassNode which represents the new node which have to be added
     */
    fun addChecklist(newNode: TestingChecklistClassNode) {

        val uiTreeRoot = uiTree.model.root as CheckedTreeNode

        // not finding the method, create new class
        if (!findClass(uiTreeRoot, newNode)) {
            dataTree.classChecklists.add(newNode)
            val classNode = CheckedTreeNode(ChecklistUserObject(newNode))
            uiTreeRoot.add(classNode)
            for (method in newNode.children) {

                val methodNode = CheckedTreeNode(ChecklistUserObject(method))

                for (item in method.children) {
                    val itemNode = CheckedTreeNode(ChecklistUserObject(item))
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
     * Gets the index of the item from a method
     *
     * @param deleteItem the TestingChecklistLeafNode which we have to find the index
     * @param methodNode the TestingChecklistMethodNode for which we have to return the index of the item
     */
    private fun getIndexItem(
        deleteItem: TestingChecklistLeafNode,
        methodNode: TestingChecklistMethodNode
    ): Int {
        for (i in 0 until methodNode.children.size) {
            val itemNode = methodNode.children[i]
            if (itemNode.description == deleteItem.description) {
                return i
            }
        }
        return -1
    }

    /**
     * Gets the index of the method from a class
     *
     * @param deleteMethod the TestingChecklistMethodNode which we have to find the index
     * @param classNode the TestingChecklistClassNode for which we have to return the index of the item
     */
    private fun getIndexMethod(
        deleteMethod: TestingChecklistMethodNode,
        classNode: TestingChecklistClassNode
    ): Int {

        for (i in 0 until classNode.children.size) {
            val methodNode = classNode.children[i]
            if (methodNode.description == deleteMethod.description) {
                return i
            }
        }
        return -1
    }

    /**
     * Gets the index of the class from the entire checklist
     *
     * @param deleteClass the TestingChecklistClassNode which we have to find the index
     */
    private fun getIndexClass(deleteClass: TestingChecklistClassNode): Int {

        for (l in 0 until dataTree.classChecklists.size) {
            val dataClass = dataTree.classChecklists[l]
            if (dataClass.description == deleteClass.description) {
                return l
            }
        }
        return -1
    }

    /**
     * Delete the item from the tree
     *
     * @param deleteItem the TestingChecklistLeafNode which we have to delete it
     * @param deleteMethod the TestingChecklistMethodNode which contains the item
     * @param deleteClass the TestingChecklistClassNode which contains the method
     */
    fun deleteItem(
        deleteItem: TestingChecklistLeafNode,
        deleteMethod: TestingChecklistMethodNode,
        deleteClass: TestingChecklistClassNode
    ) {
        val uiTreeRoot = uiTree.model.root as CheckedTreeNode
        val indexClass = getIndexClass(deleteClass)
        if (indexClass == -1) { return }

        val classNode = dataTree.classChecklists[indexClass]
        val uiTreeClass = uiTreeRoot.getChildAt(indexClass) as CheckedTreeNode

        val indexMethod = getIndexMethod(deleteMethod, classNode)
        if (indexMethod == -1) { return }
        val methodNode = classNode.children[indexMethod]
        val uiTreeMethod = uiTreeClass.getChildAt(indexMethod) as CheckedTreeNode

        val indexItem = getIndexItem(deleteItem, methodNode)
        if (indexItem == -1) { return }

        if (deleteItem.checked > 0) {
            methodNode.checked -= deleteItem.checked
            classNode.checked -= deleteItem.checked
        }

        methodNode.children.removeAt(indexItem)
        uiTreeMethod.remove(indexItem)
        (uiTree.model as DefaultTreeModel).reload()
        TreeUtil.expandAll(uiTree)
    }

    /**
     * Delete the method from the tree
     *
     * @param deleteMethod the TestingChecklistMethodNode which we have to delete it
     * @param deleteClass the TestingChecklistClassNode which contains the method
     */
    fun deleteMethod(
        deleteMethod: TestingChecklistMethodNode,
        deleteClass: TestingChecklistClassNode
    ) {
        val uiTreeRoot = uiTree.model.root as CheckedTreeNode
        val indexClass = getIndexClass(deleteClass)
        if (indexClass == -1) { return }

        val classNode = dataTree.classChecklists[indexClass]
        val uiTreeClass = uiTreeRoot.getChildAt(indexClass) as CheckedTreeNode
        val indexMethod = getIndexMethod(deleteMethod, classNode)

        if (indexMethod == -1) { return }

        if (deleteMethod.checked > 0) {
            classNode.checked -= deleteMethod.checked
        }

        classNode.children.removeAt(indexMethod)
        uiTreeClass.remove(indexMethod)
        (uiTree.model as DefaultTreeModel).reload()
        TreeUtil.expandAll(uiTree)
    }

    /**
     * Delete the delete from the tree
     *
     * @param deleteClass the TestingChecklistClassNode which we have to delete it
     */
    fun deleteClass(deleteClass: TestingChecklistClassNode) {

        val uiTreeRoot = uiTree.model.root as CheckedTreeNode
        val index = getIndexClass(deleteClass)
        if (index > -1) {
            uiTreeRoot.remove(index)
            dataTree.classChecklists.removeAt(index)
        }
        (uiTree.model as DefaultTreeModel).reload()
        TreeUtil.expandAll(uiTree)
    }

    /**
     * Delete a node from the tree.
     *
     * @param node The CheckedTreeNode which have to be deleted
     */
    fun deleteElement(node: CheckedTreeNode) {

        if ((node.userObject as ChecklistUserObject).checklistNode is TestingChecklistClassNode) {

            deleteClass(
                (node.userObject as ChecklistUserObject)
                    .checklistNode as TestingChecklistClassNode
            )
        } else if ((node.userObject as ChecklistUserObject)
            .checklistNode is TestingChecklistMethodNode
        ) {

            deleteMethod(
                (node.userObject as ChecklistUserObject)
                    .checklistNode as TestingChecklistMethodNode,
                ((node.parent as CheckedTreeNode).userObject as ChecklistUserObject)
                    .checklistNode as TestingChecklistClassNode
            )
        } else if ((node.userObject as ChecklistUserObject)
            .checklistNode is TestingChecklistLeafNode
        ) {

            deleteItem(
                (node.userObject as ChecklistUserObject).checklistNode as TestingChecklistLeafNode,
                ((node.parent as CheckedTreeNode).userObject as ChecklistUserObject)
                    .checklistNode as TestingChecklistMethodNode,
                ((node.parent.parent as CheckedTreeNode).userObject as ChecklistUserObject)
                    .checklistNode as TestingChecklistClassNode
            )
            UsageDataService.instance.recordItemDeleted()
        }
    }

    /**
     * This function returns a String which represents the tree.
     */
    fun print(): String {
        var printString: String = ""
        for (dataClass in dataTree.classChecklists) {
            for (dataMethod in dataClass.children) {
                for (dataItem in dataMethod.children) {
                    printString = printString + dataClass.description + " " + dataMethod.description
                    printString = printString + " " + dataItem.description + "\n"
                }
            }
        }
        return printString
    }
}
