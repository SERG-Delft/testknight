package com.testbuddy.services

import com.testbuddy.models.*

class ChecklistTree {

    private var node: ChecklistNode? = null
    private var parent: ChecklistTree? = null
    private var children: MutableList<ChecklistTree>

//    constructor()

    private constructor(node: ChecklistNode) {
        this.node = node
        children = mutableListOf()
    }

    fun initTree() {
        //this.node = node
        children = mutableListOf()
    }
    fun addChild(newNode: ChecklistTree) {
        children.add(newNode)
    }

    fun updateParent(newParent: ChecklistTree) {
        parent = newParent
    }

    fun updateNode(newNode: ChecklistNode) {
        node = newNode
    }

    fun deleteParent() {
        parent = null
    }
    fun deleteChild(deleteNode: ChecklistNode) {
        children.remove(deleteNode)
    }

//    private fun addNode(currentTree: ChecklistTree, newNode:TestingChecklistNode): ChecklistTree {
//        if(newNode is TestingChecklistLeafNode)
//           return ChecklistTree(ChecklistNode(newNode.description, newNode.element, 0))]
//
//        if(newNode is TestingChecklistMethodNode)
//        {
//            currentTree
//        }
//
//    }

//    fun addNode()

//    fun addNode(newNode: TestingChecklistClassNode){
//        node = ChecklistNode(newNode.description, newNode.element, 0)
//        for( method in newNode.children)
//        {
//            var childNode = ChecklistNode( )
//            var child = ChecklistTree()
//        }
//    }

//    fun addNode(parent: ChecklistNode, newNode: TestingChecklistMethodNode){
//        node = ChecklistNode(newNode.description, newNode.element, 0)
//        for( method in newNode.children)
//        {
//            var childNode = ChecklistNode( )
//            var child = ChecklistTree()
//        }
//
//    }

//    fun addNode(parent: ChecklistTree, newNode: TestingChecklistNode) {
//        this.parent = parent
//        this.node = ChecklistNode(newNode.description, newNode.element, 0)
//    }

    fun getNode(): ChecklistNode?{
        return node 
    }

    fun addNode(newNode: TestingChecklistLeafNode): ChecklistTree {
        var newTree = ChecklistTree(ChecklistNode(newNode.description, newNode.element, 0, true))
        this.children.add(newTree)
        newTree.parent = this.parent
        return newTree
    }

    fun addNode(newNode: TestingChecklistMethodNode): ChecklistTree {
        var newTree = ChecklistTree(ChecklistNode(newNode.description, newNode.element, 0, false))
        this.children.add(newTree)
        newTree.parent = this.parent
        for (item in newNode.children) {
            newTree.addNode(item)
        }
        return newTree
    }

    fun addNode(newNode: TestingChecklistClassNode): ChecklistTree {
        var newTree = ChecklistTree(ChecklistNode(newNode.description, newNode.element, 0, false))
        this.children.add(newTree)
        newTree.parent = this.parent
        for (method in newNode.children) {
            newTree.addNode(method)
        }
        return newTree
    }
}
