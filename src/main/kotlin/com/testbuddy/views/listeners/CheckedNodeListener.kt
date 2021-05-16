package com.testbuddy.views.listeners

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckboxTreeHelper
import com.intellij.ui.CheckboxTreeListener
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.models.TestingChecklistLeafNode

/**
* Custom CheckboxTreelistener which support counting the selected items.
*
* @param checklistTree CheckboxTree the listener listens to.
*/
class CheckedNodeListener(private val checklistTree: CheckboxTree) : CheckboxTreeListener {

    override fun nodeStateChanged(node: CheckedTreeNode) {

        val x = Tree.NodeFilter<TestingChecklistLeafNode> { true }
        println(
            "CHECKLIST ITEM: ${
            CheckboxTreeHelper.getCheckedNodes<TestingChecklistLeafNode>
            (TestingChecklistLeafNode::class.java, x, checklistTree.model).size}"
        )
    }
}
