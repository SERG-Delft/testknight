
import com.intellij.ui.components.JBLabel
import com.testbuddy.com.testbuddy.models.TestClassData
import com.testbuddy.models.TestMethodData
import java.awt.Component
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer

class CopyPasteCellRenderer : DefaultTreeCellRenderer() {

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
            val mPanel = JPanel()

            mPanel.layout = BoxLayout(mPanel, BoxLayout.LINE_AXIS)

            if (value.userObject is TestMethodData) {

                val method = (value.userObject as TestMethodData)

                // Create the labels and buttons.
                // The glue adds spacing/moves the button to the right

                mPanel.add(JBLabel(method.name))
                mPanel.add(Box.createHorizontalGlue())

                val copyButton = JButton("Copy")
                // copyButton.addActionListener(ButtonCopyClickListener(method, event))

                mPanel.add(copyButton)

                val gotoButton = JButton("Goto")

                mPanel.add(gotoButton)
                // Limit size of the panel
//                mPanel.minimumSize = Dimension(0, 50)
//                mPanel.maximumSize = Dimension(Integer.MAX_VALUE, 50)
//                mPanel.setSize(-1, 50)
                return mPanel
//            copyPastePanel.add(mPanel)
            }
            else if(value.userObject is TestClassData){
                val className = (value.userObject as TestClassData)

                // Create the label with the class name

                mPanel.add(JBLabel(className.name))
                mPanel.add(Box.createHorizontalGlue())

                // copyButton.addActionListener(ButtonCopyClickListener(method, event))

                // Limit size of the panel
//                mPanel.minimumSize = Dimension(0, 50)
//                mPanel.maximumSize = Dimension(Integer.MAX_VALUE, 50)
//                mPanel.setSize(-1, 50)
                return mPanel
            }
        }
        return JLabel()
    }
}
