package com.testbuddy.listeners

import javax.swing.event.TreeSelectionEvent
import javax.swing.event.TreeSelectionListener
import javax.swing.tree.DefaultMutableTreeNode

class TestListSelectionListener : TreeSelectionListener {

    /**
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     */
    override fun valueChanged(e: TreeSelectionEvent?) {
        val component = (e?.newLeadSelectionPath ?: return).lastPathComponent ?: return

        if (component is DefaultMutableTreeNode) {
            println(component.userObject)
        }
    }
}
