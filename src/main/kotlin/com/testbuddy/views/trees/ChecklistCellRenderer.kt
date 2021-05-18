package com.testbuddy.views.trees

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.SimpleTextAttributes
import com.testbuddy.models.*
import javax.swing.JTree

/**
 * Custom cell renderer for Checklist which has checkboxes for checklist items.
 */
class ChecklistCellRenderer(opaque: Boolean) : CheckboxTree.CheckboxTreeCellRenderer(opaque) {

    /**
     * Shows the number of items checked per class and method.
     * The checklist item has a checkbox which the user can check.
     *
     * @param tree The tree of the renderer.
     * @param value The node which needs to be rendered.
     * @param selected is this path selected?
     * @param leaf is this path a leaf?
     * @param row row of the path
     * @param hasFocus is this path focused?
     * @return The corresponding component based on the param: value.
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

//        if (value is CheckedTreeNode) {
//            val renderer = textRenderer
//            if (value.userObject is ChecklistUserObject) {
//                val userObject = value.userObject as ChecklistUserObject
//
//                val checklistNode = userObject.checklistNode
//                when (checklistNode) {
//                    is TestingChecklistParentNode -> {
//                        checkbox.isVisible = false
//                        checkbox.isEnabled = false
//                    }
//                }
//
//                val name = getDescription((value.userObject as ChecklistUserObject).checklistNode)
//                renderer.append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)
//
//                if (checklistNode !is TestingChecklistLeafNode) {
//                    val countString = " ${userObject.checkCount} item(s) checked."
//                    renderer.append(countString, SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES)
//                }
//            }
//        }

        // Version 2
//ChecklistNode(var description: String, val element: PsiElement, var checkCount: Int, val isItem: Boolean)
        if (value is CheckedTreeNode) {
                    val renderer = textRenderer
                    if (value.userObject is ChecklistNode) {
                        val userObject = value.userObject as ChecklistNode


                            if(userObject.depth!=3) {
                                checkbox.isVisible = false
                                checkbox.isEnabled = false
                            }


                        val name = userObject.description
                        renderer.append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)

                        if(userObject.depth!=3) {
                            val countString = " ${userObject.checkCount} item(s) checked."
                            renderer.append(countString, SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES)
                        }
                    }
                }
    }

    /**
     * Gets the description from TestingChecklistNode
     *
     * @param node TestingChecklistNode of the checklist tree.
     * @return Description of the node.
     */
    private fun getDescription(node: TestingChecklistNode): String {
        var name: String = ""
        when (node) {
            is TestingChecklistLeafNode -> name = node.description
            is TestingChecklistMethodNode -> name = node.description
            is TestingChecklistClassNode -> name = node.description
        }
        return name
    }
}
