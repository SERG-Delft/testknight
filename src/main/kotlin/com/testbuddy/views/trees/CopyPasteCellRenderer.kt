
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.components.JBLabel
import com.testbuddy.models.TestMethodData
import java.awt.Component
import java.awt.Dimension
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode

import javax.swing.tree.DefaultTreeCellRenderer

import javax.swing.tree.TreeCellRenderer


class CopyPasteCellRenderer: ColoredTreeCellRenderer() {
    protected val leafRenderer = JCheckBox()
    private val nonLeafRenderer = DefaultTreeCellRenderer()





//    override fun getTreeCellRendererComponent(
//        tree: JTree, value: Any, selected: Boolean,
//        expanded: Boolean, leaf: Boolean, row: Int, hasFocus: Boolean
//    ): Component {
//        val returnValue: Component
//        if (leaf) {
//            val stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, false)
//            leafRenderer.text = stringValue
//            leafRenderer.isSelected = false
//            leafRenderer.isEnabled = tree.isEnabled
//            if (value != null && value is DefaultMutableTreeNode) {
//                val userObject = value.userObject
//                if (userObject is CheckBoxNode) {
//                    val node: CheckBoxNode = userObject as CheckBoxNode
//                    leafRenderer.text = node.getText()
//                    leafRenderer.isSelected = node.isSelected()
//                }
//            }
//            returnValue = leafRenderer
//        } else {
//            returnValue = nonLeafRenderer.getTreeCellRendererComponent(
//                tree, value, selected, expanded,
//                leaf, row, hasFocus
//            )
//        }
//        return returnValue
//    }

    override fun customizeCellRenderer(
        tree: JTree,
        value: Any?,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ) {

        if(value is DefaultMutableTreeNode){
            val mPanel = JPanel()

            mPanel.layout = BoxLayout(mPanel, BoxLayout.LINE_AXIS)

            val method = value.userObject as TestMethodData

            // Create the labels and buttons.
            // The glue adds spacing/moves the button to the right

            mPanel.add(JBLabel(method.name))
            mPanel.add(Box.createHorizontalGlue())

            val copyButton = JButton("Copy")
            //copyButton.addActionListener(ButtonCopyClickListener(method, event))

            mPanel.add(copyButton)

            val gotoButton = JButton("Goto")

            mPanel.add(gotoButton)
            // Limit size of the panel
            mPanel.minimumSize = Dimension(0, 50)
            mPanel.maximumSize = Dimension(Integer.MAX_VALUE, 50)
            mPanel.setSize(-1, 50)
//            copyPastePanel.add(mPanel)

        }

    }

}