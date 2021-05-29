package com.testbuddy.views.trees

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.SimpleTextAttributes
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testbuddy.com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testbuddy.com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistParentNode
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.TestingChecklistNode
import java.awt.Color
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

                val name = getDescription((value.userObject as ChecklistUserObject).checklistNode)
                renderer.append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)

                if (checklistNode !is TestingChecklistLeafNode) {
                    val countCheckedNodes = checklistNode.checked
                    val countChildrenNodes = getChildCount(userObject.checklistNode as TestingChecklistParentNode)
                    val countBoxes = " $countCheckedNodes/" + "$countChildrenNodes item(s) checked."

                    colorItemsStatistics(countCheckedNodes, countChildrenNodes, renderer, countBoxes)
                }
            }
        }
    }

    /**
     * Sets the color for the items statistics(grey/green)
     */
    @Suppress("MagicNumber")
    private fun colorItemsStatistics(
        countCheckedNodes: Int,
        countChildrenNodes: Int,
        renderer: ColoredTreeCellRenderer,
        countBoxes: String
    ) {
        val red = 22
        val green = 102
        val blue = 20
        if (countCheckedNodes == countChildrenNodes && countChildrenNodes != 0) {
            renderer.append(
                countBoxes,
                SimpleTextAttributes(SimpleTextAttributes.STYLE_ITALIC, Color(red, green, blue))
            )
        } else {
            renderer.append(countBoxes, SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES)
        }
    }

    /**
     * Gets the number of the boxes from the children
     * @param node TestingChecklistParentNode for which we have to compute the number of checklist
     */
    private fun getChildCount(node: TestingChecklistParentNode): Int {
        var count: Int = 0
        when (node) {
            is TestingChecklistMethodNode -> count += node.children.size
            is TestingChecklistClassNode -> {
                for (child in node.children) {
                    count += getChildCount(child)
                }
            }
        }
        return count
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
