package com.testbuddy.com.testbuddy.views.trees

import com.intellij.ui.components.JBLabel
import com.testbuddy.models.TestMethodData
import java.awt.Component
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeCellRenderer


class CopyPasteCellRenderer() : JPanel(), TreeCellRenderer {

    var methodLabel : JBLabel? = null
    var copyButton : JButton? = null
    var gotoButton : JButton? = null
    var horizontalGlue : Component?  = null

    init {
        this.layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        methodLabel = JBLabel()
        horizontalGlue = Box.createHorizontalGlue()
        copyButton  = JButton("Copy")
        gotoButton  = JButton("Goto")
        add(methodLabel)
        add(horizontalGlue)
        add(copyButton)
        add(gotoButton)
    }


    override fun getTreeCellRendererComponent(
        tree: JTree,
        value: Any,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ): Component {
        if (value is DefaultMutableTreeNode) {

            if (value.userObject is List<*>) {

                val method = ((value.userObject as List<*>)[0] as TestMethodData)

                methodLabel!!.text = method.name

                return this

            }
        }
        return JBLabel("TEST")
    }


}
