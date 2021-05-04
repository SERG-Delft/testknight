package com.testbuddy.com.testbuddy.views

import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckboxTreeBase.CheckboxTreeCellRendererBase
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
        if(value is DefaultMutableTreeNode) {
            this.remove(checkbox)
            val text: String = value.userObject as String
            val renderer = textRenderer
            renderer.append(text, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        } else if (value is CheckedTreeNode) {
            val text: String = value.userObject as String
            val renderer = textRenderer
            renderer.append(text, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        }
    }
}
