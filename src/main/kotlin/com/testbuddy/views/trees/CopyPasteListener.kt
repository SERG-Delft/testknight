package com.testbuddy.com.testbuddy.views.trees

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.ui.ClickListener
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.models.TestMethodData
import com.testbuddy.services.DuplicateTestsService
import com.testbuddy.services.GotoTestService
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseEvent
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath


/**
 * This listener checks if you pressed the copy or goto button.
 * Since the renderer isn't a live component, this is one way to make it "clickable".
 */
class CopyPasteListener(private val tree: Tree, private val cellRenderer: CopyPasteCellRenderer) : ClickListener() {

    override fun onClick(e: MouseEvent, clickCount: Int): Boolean {

        //Find which node is being selected.
        val path : TreePath = tree.selectionPath ?: return false

        if(path.lastPathComponent !is DefaultMutableTreeNode) return false

        val node = path.lastPathComponent as DefaultMutableTreeNode

        if (node.userObject is List<*>) {

            val rowBounds: Rectangle? = tree.getPathBounds(path)

            val copyBounds: Rectangle = cellRenderer.copyButton!!.bounds
            val gotoBounds: Rectangle = cellRenderer.gotoButton!!.bounds
            copyBounds.location = rowBounds!!.location
            gotoBounds.location = rowBounds.location

            //The panel is structured as :
            //[label--horizontalglue----------copyButton-gotoButton]
            val x = (rowBounds.location.x + cellRenderer.methodLabel!!.bounds.width + cellRenderer.horizontalGlue!!.bounds.width)

            //Update the button bound location based on the label, glue width (and copy button).
            copyBounds.location = Point(x, copyBounds.y)
            gotoBounds.location = Point(x+cellRenderer.copyButton!!.bounds.width, gotoBounds.y)


            val reference = ((node.userObject as List<*>)[0] as TestMethodData)
            val event = ((node.userObject as List<*>)[1] as AnActionEvent)

            if (copyBounds.contains(e.point)) {
                val duplicateTestsService = event.project!!.service<DuplicateTestsService>()
                val editor = event.getData(CommonDataKeys.EDITOR)

                if (editor != null) {
                    duplicateTestsService.duplicateMethod(reference.psiMethod, editor)
                    return true
                }
            } else if (gotoBounds.contains(e.point)) {
                val gotoTestService = event.project!!.service<GotoTestService>()
                val editor = event.getData(CommonDataKeys.EDITOR)

                if (editor != null) {
                    gotoTestService.gotoMethod(editor, reference)
                    return true
                }
            }
        }

        return false
    }
}