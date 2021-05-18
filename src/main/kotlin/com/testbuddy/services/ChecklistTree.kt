package com.testbuddy.services

import com.testbuddy.models.*
import java.util.*

class ChecklistTree {

    private var node: ChecklistNode? = null
    private var parent: ChecklistTree? = null
    private var children: MutableList<ChecklistTree>
    private var allNodes: MutableSet<ChecklistNode>
    private lateinit var mapNodes: MutableMap<Int, ChecklistTree>
    private lateinit var unusedIndex: Queue<Int>

//    constructor()

    private constructor(node: ChecklistNode, allNodes: MutableSet<ChecklistNode>, mapNodes: MutableMap<Int, ChecklistTree>, unusedIndex: Queue<Int>) {
        this.node = node
        children = mutableListOf()
        this.allNodes = allNodes
        this.mapNodes = mapNodes
        this.unusedIndex = unusedIndex
    }

    constructor(){
        children = mutableListOf()
        allNodes = mutableSetOf()
        mapNodes = mutableMapOf()
        unusedIndex = LinkedList<Int>()
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

    fun checkAddition(node : ChecklistNode):Boolean{
        return !allNodes.contains(node)
    }


    fun addNode(newNode: TestingChecklistLeafNode): ChecklistTree {
        val index = getIndex()
        val newChecklistNode = ChecklistNode(newNode.description, newNode.element, 0, 3, index)
        this.allNodes.add(newChecklistNode)
        var newTree = ChecklistTree(newChecklistNode, allNodes, mapNodes, unusedIndex)
        this.children.add(newTree)
        newTree.parent = this.parent
        mapNodes[index] = newTree
        return newTree
    }

    private fun getIndex() : Int{
        if(!unusedIndex.isEmpty())
            return mapNodes.size
        else return unusedIndex.remove()
    }

    fun addNode(newNode: TestingChecklistMethodNode): ChecklistTree {
        val index = getIndex()
        val newChecklistNode = ChecklistNode(newNode.description, newNode.element, 0, 2, index)
        this.allNodes.add(newChecklistNode)
        var newTree = ChecklistTree(newChecklistNode, allNodes, mapNodes, unusedIndex)
        this.children.add(newTree)
        newTree.parent = this.parent
        for (item in newNode.children) {
            newTree.addNode(item)
        }
        mapNodes[index] = newTree
        return newTree
    }

    fun addNode(newNode: TestingChecklistClassNode): ChecklistTree {
        val index = getIndex()
        val newChecklistNode = ChecklistNode(newNode.description, newNode.element, 0, 1, index)
        this.allNodes.add(newChecklistNode)
        var newTree = ChecklistTree(newChecklistNode, allNodes, mapNodes, unusedIndex)
        this.children.add(newTree)
        newTree.parent = this.parent
        for (method in newNode.children) {
            newTree.addNode(method)
        }
        mapNodes[index] = newTree
        return newTree
    }

   fun delete(index: Int)
   {
       val deleteTree = mapNodes[index] ?: return
       mapNodes.remove(index, deleteTree)
       unusedIndex.add(index)
       val parentNode = deleteTree.parent ?: return
      for (child in parentNode.children)
      {
          if (child.node?.index == index)
              parentNode.children.remove(deleteTree)
      }
       deleteTree.parent = null
   }

    fun edit(index: Int, description: String){
        val editTree = mapNodes[index] ?: return
        var newNode = ChecklistNode(description, editTree.node!!.element, editTree.node!!.checkCount, editTree.node!!.depth, index)
        ///possible to modify the counter
        editTree.node  = newNode
    }


}
