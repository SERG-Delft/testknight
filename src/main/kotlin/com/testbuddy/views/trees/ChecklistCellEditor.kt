package com.testbuddy.views.trees

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.components.JBTextField
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.services.ChecklistTreeService
import java.awt.Component
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.EventObject
import javax.swing.JTree
import javax.swing.event.CellEditorListener
import javax.swing.tree.TreeCellEditor

class ChecklistCellEditor(private val tree: Tree, private val project: Project) : TreeCellEditor, JBTextField() {

    var textField: JBTextField = JBTextField()

    /**
     * Returns the value contained in the editor.
     * @return the value contained in the editor
     */
    override fun getCellEditorValue(): Any {
        return textField.text
    }

    /**
     * Asks the editor if it can start editing using `anEvent`.
     * `anEvent` is in the invoking component coordinate system.
     * The editor can not assume the Component returned by
     * `getCellEditorComponent` is installed.  This method
     * is intended for the use of client to avoid the cost of setting up
     * and installing the editor component if editing is not possible.
     * If editing can be started this method returns true.
     *
     * @param anEvent         the event the editor should use to consider
     * whether to begin editing or not
     * @return true if editing can be started
     * @see .shouldSelectCell
     */
    override fun isCellEditable(event: EventObject?): Boolean {
        return true
    }
//
//        println("am ajuns la is Cell Editable" + event)
//
//        if(event is MouseEvent )
//        {
//            println("am trecut primul if")
//            if (SwingUtilities.isRightMouseButton(event as MouseEvent)) {
//
//                val path: TreePath = tree.selectionPath ?: return false
//
//                if (path.lastPathComponent !is DefaultMutableTreeNode) return false
//
//                val node = path.lastPathComponent as CheckedTreeNode
//
//                val menu = JBPopupMenu()
//                val delete = JBMenuItem("Delete")
//                delete.addActionListener(DeleteChecklistAction(node, project))
//                menu.add(delete)
//
//                if ((node.userObject as ChecklistUserObject).checklistNode is TestingChecklistLeafNode) {
//                    val edit = JBMenuItem("Edit")
//
//
//                    edit.addActionListener{it.apply {  tree.startEditingAtPath(path) }
//
//                        //println("kaokowk")
//                    }
//                    menu.add(edit)
//                } else if ((node.userObject as ChecklistUserObject).checklistNode is TestingChecklistMethodNode) {
//                    val addItem = JBMenuItem("Add item")
//                    addItem.addActionListener(DeleteChecklistAction(node, project))
//                    menu.add(addItem)
//                }
//
//                menu.show(tree, event.x, event.y)
//
//                // If the node contains the userObject which is expected from a test method node.
//                println(node.userObject)
//                println(node.parent)
//                println("final editor")
//                return true
//            }
//        }
//        return false
//
//    }

//        var returnValue = false
//        if (event is MouseEvent) {
//            val mouseEvent: MouseEvent = event as MouseEvent
//            val path: TreePath = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY())
//            if (path != null) {
//                val node: Any = path.getLastPathComponent()
//                if (node != null && node is DefaultMutableTreeNode) {
//                    val treeNode = node
//                   /// val userObject = treeNode.userObject
//                    returnValue = ((node.userObject as ChecklistUserObject).checklistNode is TestingChecklistLeafNode)
//                    println("ama ajuns aici")
//                }
//            }
//        }
//        return returnValue

    /**
     * Returns true if the editing cell should be selected, false otherwise.
     * Typically, the return value is true, because is most cases the editing
     * cell should be selected.  However, it is useful to return false to
     * keep the selection from changing for some types of edits.
     * eg. A table that contains a column of check boxes, the user might
     * want to be able to change those checkboxes without altering the
     * selection.  (See Netscape Communicator for just such an example)
     * Of course, it is up to the client of the editor to use the return
     * value, but it doesn't need to if it doesn't want to.
     *
     * @param anEvent         the event the editor should use to start
     * editing
     * @return true if the editor would like the editing cell to be selected;
     * otherwise returns false
     * @see .isCellEditable
     */
    override fun shouldSelectCell(anEvent: EventObject?): Boolean {
        return true
    }

    /**
     * Tells the editor to stop editing and accept any partially edited
     * value as the value of the editor.  The editor returns false if
     * editing was not stopped; this is useful for editors that validate
     * and can not accept invalid entries.
     *
     * @return true if editing was stopped; false otherwise
     */
    override fun stopCellEditing(): Boolean {
        return true
    }

    /**
     * Tells the editor to cancel editing and not accept any partially
     * edited value.
     */
    override fun cancelCellEditing() {
        textField.text = "kawkekkokoakokokkkokokok"
    }

    /**
     * Adds a listener to the list that's notified when the editor
     * stops, or cancels editing.
     *
     * @param l               the CellEditorListener
     */
    override fun addCellEditorListener(l: CellEditorListener?) {

        println("am ajuns la cell edditor" + l)
    }

    /**
     * Removes a listener from the list that's notified
     *
     * @param l               the CellEditorListener
     */
    @Suppress("EmptyFunctionBlock")
    override fun removeCellEditorListener(l: CellEditorListener?) {
    }

    /**
     * Sets an initial <I>value</I> for the editor.  This will cause
     * the editor to stopEditing and lose any partially edited value
     * if the editor is editing when this method is called.
     *
     *
     *
     * Returns the component that should be added to the client's
     * Component hierarchy.  Once installed in the client's hierarchy
     * this component will then be able to draw and receive user input.
     *
     * @param tree            the JTree that is asking the editor to edit;
     * this parameter can be null
     * @param value           the value of the cell to be edited
     * @param isSelected      true if the cell is to be rendered with
     * selection highlighting
     * @param expanded        true if the node is expanded
     * @param leaf            true if the node is a leaf node
     * @param row             the row index of the node being edited
     * @return the component for editing
     */
    override fun getTreeCellEditorComponent(
        tree: JTree?,
        value: Any?,
        isSelected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int
    ): Component {

        // var textField = JBTextField()
        println("I am in get tree component")
        val userObject = ((value as CheckedTreeNode).userObject as ChecklistUserObject)
        val description = (userObject.checklistNode as TestingChecklistLeafNode).description
        textField.text = description
        println(textField.text)
        textField.isEditable = true
        //   textField.document.addDocumentListener()
        textField.addKeyListener(object :

                KeyListener {
                /**
                 * Invoked when a key has been typed.
                 * See the class description for [KeyEvent] for a definition of
                 * a key typed event.
                 * @param e the event to be processed
                 */
                override fun keyTyped(e: KeyEvent?) {
                    // /mawmaiowkaokw
                }

                /**
                 * Invoked when a key has been pressed.
                 * See the class description for [KeyEvent] for a definition of
                 * a key pressed event.
                 * @param e the event to be processed
                 */
                @Suppress("EmptyFunctionBlock")
                override fun keyPressed(e: KeyEvent?) {
                }

                /**
                 * Invoked when a key has been released.
                 * See the class description for [KeyEvent] for a definition of
                 * a key released event.
                 * @param e the event to be processed
                 */
                override fun keyReleased(e: KeyEvent?) {

                    println(e)
                    if (e != null) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            println("am apasat enter")
                            (userObject.checklistNode as TestingChecklistLeafNode).description = textField.text
                            textField.isEditable = false
                            stopCellEditing()
                        }
                    }
                }
            })

        // textField.text == "kakowkqo"

//        (userObject.checklistNode as TestingChecklistLeafNode).description = "koakwokwo"

        val service = project.service<ChecklistTreeService>()

        println(service.print())

        return textField
//        if (value is CheckedTreeNode) {
//            if (value.userObject is ChecklistUserObject) {
//

//                val name = getDescription((value.userObject as ChecklistUserObject).checklistNode)
//                renderer.append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES)
//
//                if (checklistNode !is TestingChecklistLeafNode) {
//                    val countCheckedNodes = checklistNode.checked
//                    val countChildrenNodes = getChildCount(userObject.checklistNode as TestingChecklistParentNode)
//                    val countBoxes = " $countCheckedNodes/" + "$countChildrenNodes item(s) checked."
//
//                    colorItemsStatistics(countCheckedNodes, countChildrenNodes, renderer, countBoxes)
//                }
    }

//    /**
//     * Gets the description from TestingChecklistNode
//     *
//     * @param node TestingChecklistNode of the checklist tree.
//     * @return Description of the node.
//     */
//    private fun getDescription(node: TestingChecklistNode): String {
//        var name: String = ""
//        when (node) {
//            is TestingChecklistLeafNode -> name = node.description
//            is TestingChecklistMethodNode -> name = node.description
//            is TestingChecklistClassNode -> name = node.description
//        }
//        return name
//    }
}
