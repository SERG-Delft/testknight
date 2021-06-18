package com.testknight.views.trees

import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.components.JBTextField
import com.testknight.models.ChecklistUserObject
import com.testknight.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import java.awt.Component
import java.awt.event.ActionListener
import java.util.EventObject
import javax.swing.AbstractCellEditor
import javax.swing.JTree
import javax.swing.tree.TreeCellEditor

@Suppress("MagicNumber")
class ChecklistCellEditor : TreeCellEditor, AbstractCellEditor() {

    private var textField: JBTextField = JBTextField(50)
    private lateinit var currObj: ChecklistUserObject
    private var listener: ActionListener = ActionListener { stopCellEditing() }
    /**
     * Returns the value which have been edited.
     * @return the value, which represents a TestingChecklistLeafNode which have been edited.
     */
    override fun getCellEditorValue(): Any {
        (currObj.checklistNode as TestingChecklistLeafNode).description = textField.text
        return currObj
    }

    /**
     * Checks if a specific element can be edited.
     * If editing can be started this method returns true, false otherwise.
     *
     * @param event  the event the editor should use to consider whether to begin editing or not
     * @return true if editing can be started, false otherwise
     */
    override fun isCellEditable(event: EventObject?): Boolean {
        if (event == null) {
            return true
        }
        return false
    }

    /**
     * Returns the component that should appear when a user wants to edit a TestingChecklistLeafNode.
     * In our case, it should return a TextField in which the user will put the new description of the item.
     *
     * @param tree            the JTree that is asking the editor to edit;
     * this parameter can be null
     * @param value           the value of the cell to be edited
     * @param isSelected      true if the cell is to be rendered with
     * selection highlighting
     * @param expanded        true if the node is expanded
     * @param leaf            true if the node is a leaf node
     * @param row             the row index of the node being edited
     * @return the component for editing, in our case a TextField
     */
    override fun getTreeCellEditorComponent(
        tree: JTree?,
        value: Any?,
        isSelected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int
    ): Component {

        currObj = ((value as CheckedTreeNode).userObject as ChecklistUserObject)
        val description = (currObj.checklistNode as TestingChecklistLeafNode).description
        textField.text = description

        textField.isEditable = true

        textField.removeActionListener(listener)
        textField.addActionListener(listener)

        return textField
    }
}
