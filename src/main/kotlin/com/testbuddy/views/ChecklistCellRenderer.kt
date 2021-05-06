package com.testbuddy.com.testbuddy.views

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.SimpleTextAttributes
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode

class ChecklistCellRenderer(opaque: Boolean) : CheckboxTree.CheckboxTreeCellRenderer(opaque) {

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
