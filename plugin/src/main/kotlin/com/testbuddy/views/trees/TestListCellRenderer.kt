package com.testbuddy.views.trees

import com.intellij.ui.components.JBLabel
import com.testbuddy.models.TestClassData
import com.testbuddy.models.TestMethodUserObject
import java.awt.Component
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeCellRenderer

/**
 * A custom cell renderer for the copy paste tab.
 * Since it also extends from JPanel, this component can be returned as the rendering component.
 *
 * The test nodes will have the following structure:
 *   [methodName---------------Copy-Goto]
 */
class TestListCellRenderer : JPanel(), TreeCellRenderer {

    var methodLabel: JBLabel? = null
    var copyButton: JButton? = null
    var gotoButton: JButton? = null
    var horizontalGlue: Component? = null

    /**
     * Constructor which creates the layout for the test node panel.
     */
    init {
        this.layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        methodLabel = JBLabel()
        horizontalGlue = Box.createHorizontalGlue()
        copyButton = JButton("Copy")
        gotoButton = JButton("Goto")

        copyButton!!.isOpaque = false
        gotoButton!!.isOpaque = false

        add(methodLabel)
        add(horizontalGlue)
        add(copyButton)
        add(gotoButton)
    }

    /**
     * Checks if the value is a DefaultMutableTreeNode.
     * If yes, it checks whether the userObject is that of the test method or test class.
     * If it is the test method, it updates the test node panel to have the test method name and return it.
     * else if it is the test class, it returns a JBLabel with the class name.
     * If none of the case match, it returns a empty JBLabel.
     *
     * @param tree The tree of the renderer.
     * @param value The node which needs to be rendered.
     * @param selected is this path selected?
     * @param leaf is this path a leaf?
     * @param row row of the path
     * @param hasFocus is this path focused?
     * @return The corresponding component based on the param: value.
     */
    override fun getTreeCellRendererComponent(
        tree: JTree,
        value: Any,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ): Component {

        var returnValue = JBLabel()
        if (value is DefaultMutableTreeNode) {

            if (value.userObject is TestMethodUserObject) {

                val userObject = (value.userObject as TestMethodUserObject)

                val method = userObject.reference

                methodLabel!!.text = method.name

                return this
            } else if (value.userObject is TestClassData) {
                returnValue = JBLabel((value.userObject as TestClassData).name)
            }
        }
        // Base case
        return returnValue
    }
}
