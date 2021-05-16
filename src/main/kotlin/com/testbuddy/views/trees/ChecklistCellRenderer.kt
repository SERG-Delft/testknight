package com.testbuddy.views.trees

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.SimpleTextAttributes
import com.testbuddy.models.TestingChecklistClassNode
import com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.models.TestingChecklistMethodNode
import javax.swing.JTree

class ChecklistCellRenderer(opaque: Boolean) : CheckboxTree.CheckboxTreeCellRenderer(opaque) {

    /**
     * Custom renderer for checklist tree.
     * Renders the component if it is a CheckedTreeNode
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
            var name: String = "no description"
            if (value.userObject is TestingChecklistLeafNode) {
                name = (value.userObject as TestingChecklistLeafNode).description
            } else if (value.userObject is TestingChecklistClassNode) {
                name = (value.userObject as TestingChecklistClassNode).description
                checkbox.isVisible = false
                checkbox.isEnabled = false
            } else if (value.userObject is TestingChecklistMethodNode) {
                name = (value.userObject as TestingChecklistMethodNode).description
                checkbox.isVisible = false
                checkbox.isEnabled = false
            }
            renderer.append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        }
    }
}
