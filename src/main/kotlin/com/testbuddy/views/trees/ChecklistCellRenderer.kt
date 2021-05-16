package com.testbuddy.views.trees

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.SimpleTextAttributes
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
        // Without checkbox
        if (value is DefaultMutableTreeNode) {
            val text: String = value.userObject as String
            val renderer = textRenderer
            renderer.append(text, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        } else if (value is CheckedTreeNode) {
            // with checkbox
            val text: String = value.userObject as String
            val renderer = textRenderer
            renderer.append(text, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        }
    }
}
