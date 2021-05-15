package com.testbuddy.views.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.ui.ClickListener
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.models.TestMethodUserObject
import com.testbuddy.services.DuplicateTestsService
import com.testbuddy.services.GotoTestService
import com.testbuddy.views.trees.CopyPasteCellRenderer
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseEvent
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

class CopyPasteMouseListener(private val tree: Tree, private val cellRenderer: CopyPasteCellRenderer) :
    ClickListener() {

    private var latestEditor: Editor? = null

    /**
     * Suppressing the ReturnCount warning because it makes the code messy with just 2 returns.
     *
     * This listener checks if you pressed the copy or goto button.
     * Since the renderer isn't a live component, this is one way to make it "live".
     *
     * @param e The mouse event received from the click
     * @param clickCount number of clicks
     * @return True if clicked a button, false otherwise
     */
    @Suppress("ReturnCount")
    override fun onClick(e: MouseEvent, clickCount: Int): Boolean {

        // Find which tree path is being selected.
        val path: TreePath = tree.selectionPath ?: return false

        if (path.lastPathComponent !is DefaultMutableTreeNode) return false

        val node = path.lastPathComponent as DefaultMutableTreeNode

        // If the node contains the userObject which is expected from a test method node.
        if (node.userObject is TestMethodUserObject) {

            val rowBounds: Rectangle? = tree.getPathBounds(path)

            val copyBounds: Rectangle = cellRenderer.copyButton!!.bounds
            val gotoBounds: Rectangle = cellRenderer.gotoButton!!.bounds
            copyBounds.location = rowBounds!!.location
            gotoBounds.location = rowBounds.location

            // The panel is structured as :
            // [label--horizontalglue----------copyButton-gotoButton]
            val x = (
                rowBounds.location.x +
                    cellRenderer.methodLabel!!.bounds.width +
                    cellRenderer.horizontalGlue!!.bounds.width
                )

            // Update the button bound location based on the label, glue width (and copy button).
            copyBounds.location = Point(x, copyBounds.y)
            gotoBounds.location = Point(x + cellRenderer.copyButton!!.bounds.width, gotoBounds.y)

            val userObject = node.userObject as TestMethodUserObject

            val reference = userObject.reference
            val project = userObject.project

            if (userObject.editor != null) {
                latestEditor = userObject.editor
            }

            val editor = userObject.editor ?: latestEditor

            if (copyBounds.contains(e.point)) {
                val duplicateTestsService = project!!.service<DuplicateTestsService>()
                if (editor != null) {
                    duplicateTestsService.duplicateMethod(reference.psiMethod, editor)
                    return true
                }
            } else if (gotoBounds.contains(e.point)) {
                val gotoTestService = project!!.service<GotoTestService>()

                if (editor != null) {
                    gotoTestService.gotoMethod(editor, reference)
                    return true
                }
            }
        }

        return false
    }
}
