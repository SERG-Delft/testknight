package com.testbuddy.views.trees

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.SimpleTextAttributes
import com.testbuddy.models.TestingChecklistClassNode
import com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.models.TestingChecklistMethodNode
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode

class ChecklistCellRenderer(opaque: Boolean) : CheckboxTree.CheckboxTreeCellRenderer(opaque) {

    /**
     * Custom renderer for checklist tree.
     * Renders the component if it is a DefaultMutableTreeNode(without checkbox)
     * or if it is a CheckedTreeNode(with checkbox)
     */
    override fun customizeRenderer(
        tree: JTree,
        value: Any,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ) {

        if(value is CheckedTreeNode && value.userObject is TestingChecklistLeafNode){
            val name = (value.userObject as TestingChecklistLeafNode).description
            val renderer = textRenderer
            renderer.append( name, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        }

        else if(value is DefaultMutableTreeNode)
        {
            if(value.userObject is TestingChecklistClassNode){

                val name:String = (value.userObject as TestingChecklistClassNode).description
                val renderer = textRenderer
                renderer.append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)

            }else if(value.userObject is TestingChecklistMethodNode ){
                val name:String = (value.userObject as TestingChecklistMethodNode).description
                val renderer = textRenderer
                renderer.append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)
            }
        }

    }
}
