package com.testbuddy.views.trees

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.SimpleTextAttributes
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.TestingChecklistClassNode
import com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.models.TestingChecklistMethodNode
import com.testbuddy.models.TestingChecklistNode
import com.testbuddy.models.TestingChecklistParentNode
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

        if (value is CheckedTreeNode) {
            val renderer = textRenderer
            if (value.userObject is ChecklistUserObject) {
                val userObject = value.userObject as ChecklistUserObject

                val checklistNode = userObject.checklistNode
                when (checklistNode) {
                    is TestingChecklistParentNode -> {
                        checkbox.isVisible = false
                        checkbox.isEnabled = false
                    }
                }

                val name = getDescription(value.userObject as TestingChecklistNode)
                renderer.append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)

                if (checklistNode !is TestingChecklistLeafNode) {
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
            is TestingChecklistMethodNode -> name = node.description
            is TestingChecklistClassNode -> name = node.description
            is TestingChecklistLeafNode -> name = node.description
        }
        return name
    }
}
