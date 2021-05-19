package com.testbuddy.services

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.util.ui.tree.TreeUtil
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.TestingChecklist
import com.testbuddy.models.TestingChecklistClassNode
import javax.swing.tree.DefaultTreeModel

class ChecklistTreeService {
    //  var dataTree: ChecklistTree? = null
    lateinit var uiTree: CheckboxTree

    lateinit var dataTree: TestingChecklist

    fun initTrees(uiTree: CheckboxTree) {
        dataTree = TestingChecklist(mutableListOf())
        this.uiTree = uiTree
    }

    // val classNode = CheckedTreeNode(ChecklistUserObject(checklistClassTree!!, 0))
//
//
//        // All userObjects start with check count 0
//        for (method in checklistClassTree.children) {
//
//            val methodNode = CheckedTreeNode(ChecklistUserObject(method, 0))
//
//            for (item in method.children) {
//                val itemNode = CheckedTreeNode(ChecklistUserObject(item, 0))
//                itemNode.isChecked = false
//                methodNode.add(itemNode)
//            }
//            classNode.add(methodNode)
//        }
//        (checklistTree.model.root as CheckedTreeNode).add(classNode)

    /**
     * Adds a new TestingChecklistClassNode if it was not there before
     * @param newNode TestingChecklistClassNode which represents the new node which have to be added
     */
    @Suppress("LongMethod", "ComplexMethod", "NestedBlockDepth", "LoopWithTooManyJumpStatements")
    fun addChecklist(newNode: TestingChecklistClassNode) {

        val uiTreeRoot = uiTree.model.root as CheckedTreeNode
        var foundClass: Boolean = false
        for (l in 0 until dataTree.classChecklists.size) {
            val dataClass = dataTree.classChecklists[l]
            val uiTreeClassNode = uiTreeRoot.getChildAt(l)
            if (dataClass.element == newNode.element) {
                foundClass = true

                for (methodNode in newNode.children) {
                    var foundMethod: Boolean = false
                    for (k in 0 until dataClass.children.size) {

                        val dataMethod = dataClass.children[k]
                        val uiTreeMethod = uiTreeClassNode.getChildAt(k)

                        println(dataMethod.element)
                        println(methodNode.element)

                        if (dataMethod.element == methodNode.element) {
                            foundMethod = true
                            for (itemNode in methodNode.children) {
                                var foundItem: Boolean = false
                                for (i in 0 until dataMethod.children.size) {

                                    val dataItem = dataMethod.children[i]
                                    // var uiTreeItem = uiTreeMethod.getChildAt(i)
                                    if (itemNode.description == dataItem.description &&
                                        itemNode.element == dataItem.element
                                    ) {
                                        foundItem = true
                                        // return
                                        break
                                        // i do not have to add this item
                                    }
                                }
                                if (foundItem == false && dataMethod.children.size != 0) {
                                    dataMethod.children.add(itemNode)

                                    val itemNode = CheckedTreeNode(ChecklistUserObject(itemNode, 0))
                                    itemNode.isChecked = false
                                    (uiTreeMethod as CheckedTreeNode).add(itemNode)
                                    (uiTreeClassNode as CheckedTreeNode).add(uiTreeMethod)
//                                    (uiTree.model as DefaultTreeModel).reload()
//                                    TreeUtil.expandAll(uiTree)
                                    break
                                } else {
                                    // method already exists and doesn't have a child
                                    break
                                }
                            }
                        }
                    }
                    // not finding the method, crate new method
                    if (foundMethod == false) {
                        dataClass.children.add(methodNode)
                        val uiTreeMethod = CheckedTreeNode(ChecklistUserObject(methodNode, 0))
                        for (item in methodNode.children) {
                            val itemNode = CheckedTreeNode(ChecklistUserObject(item, 0))
                            itemNode.isChecked = false
                            uiTreeMethod.add(itemNode)
                        }
                        (uiTreeClassNode as CheckedTreeNode).add(uiTreeMethod)
//                        (uiTree.model as DefaultTreeModel).reload()
//                        TreeUtil.expandAll(uiTree)
                    }
                }
            }
            break
        }
        // not finding the method, create new class
        println("2here")
        if (foundClass == false) {
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
    @Suppress("NestedBlockDepth")
    fun editChecklist(oldClassNode: TestingChecklistClassNode, newDescription: String) {

        for (dataClass in dataTree.classChecklists) {
            println("aici")
            if (dataClass.element == oldClassNode.element) {
                for (methodNode in oldClassNode.children) {
                    for (dataMethod in dataClass.children) {
                        if (dataMethod.element == methodNode.element) {
                            for (itemNode in methodNode.children) {
                                for (dataItem in dataMethod.children) {
                                    if (itemNode.description == dataItem.description &&
                                        itemNode.element == dataItem.element
                                    ) {
                                        itemNode.description = newDescription
                                        return
                                        // I edit the description of the old node
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
