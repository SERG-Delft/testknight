package com.testbuddy.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.refactoring.suggested.startOffset
import com.intellij.ui.treeStructure.Tree
import com.testbuddy.models.TestClassData
import com.testbuddy.models.TestMethodUserObject
import com.testbuddy.services.DuplicateTestsService
import com.testbuddy.services.GotoTestService
import com.testbuddy.services.UsageDataService
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreePath

/**
 * Custom keyboard listener to support copy and goto by keyboard press when focused on tree.
 *
 * @param tree Tree the listener listens to.
 * @param project The current open project for which the tree shows information.
 */
class TestListKeyboardListener(private val tree: Tree, private val project: Project) : KeyAdapter() {

    /**
     * Goes to the test method/class if ENTER has been pressed.
     * Duplicates the test method if SHIFT is also held along with pressing enter.
     *
     * @param e The key press event.
     */
    override fun keyPressed(e: KeyEvent) {
        if (e.keyCode == KeyEvent.VK_ENTER) {
            val path: TreePath = tree.selectionPath ?: return

            if (path.lastPathComponent !is DefaultMutableTreeNode) return

            val node = path.lastPathComponent as DefaultMutableTreeNode

            if (node.userObject is TestMethodUserObject) {
                val userObject = node.userObject as TestMethodUserObject

                // Copy if its shift + enter
                if (e.isShiftDown) {
                    val duplicateTestsService = project.service<DuplicateTestsService>()
                    duplicateTestsService.duplicateMethod(userObject.reference.psiMethod, userObject.editor!!)
                    UsageDataService.instance.logDuplicateTest()
                } else {
                    // else goto
                    val gotoTestService = project.service<GotoTestService>()
                    gotoTestService.gotoMethod(userObject.editor!!, userObject.reference)
                    UsageDataService.instance.logGotoTest()
                }
            } else if (node.userObject is TestClassData) {
                val userObject = node.userObject as TestClassData
                val editorList = FileEditorManager.getInstance(project).selectedEditors
                if (editorList.isNotEmpty()) {
                    val editor = (editorList[0] as TextEditor).editor

                    editor.caretModel.primaryCaret.moveToOffset(userObject.psiClass.startOffset)
                    editor.scrollingModel.scrollToCaret(ScrollType.CENTER)
                }
            }
            e.consume()
        }
    }
}
