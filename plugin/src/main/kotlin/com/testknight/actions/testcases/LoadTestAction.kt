package com.testknight.actions.testcases

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import com.testknight.models.TestMethodUserObject
import com.testknight.services.LoadTestsService
import com.testknight.utilities.UserInterfaceHelper
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class LoadTestAction : AnAction() {

    private var latestFile: PsiFile? = null

    /**
     * Updates the TestList tab to load the test cases from the selected file.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(event: AnActionEvent) {

        val project = event.project!!

        val textEditor = (FileEditorManager.getInstance(project).selectedEditor as TextEditor)
        val editor = textEditor.editor
        val psiFile = PsiManager.getInstance(project).findFile(textEditor.file!!) ?: return

        actionPerformed(project, psiFile, editor)
    }

    /**
     * Updates the TestList tab to load the test cases from the selected file.
     *
     * @param project current open project
     * @param psiFile PsiFile of the current open file
     * @param editor Editor of the current open file
     */
    fun actionPerformed(project: Project, psiFile: PsiFile, editor: Editor) {

        val loadTestsService = project.service<LoadTestsService>()
        val listClasses = loadTestsService.getTestsTree(psiFile)
        if (listClasses.isEmpty()) {
            return
        }

        val testListViewport = UserInterfaceHelper.getTabViewport(project, "Test List") ?: return
        val testListTree = testListViewport.view as Tree
        val root = testListTree.model.root as DefaultMutableTreeNode
        root.removeAllChildren()

        for (testClass in listClasses) {

            val classNode = DefaultMutableTreeNode(testClass)
            for (method in testClass.methods) {

                val testUserObject = TestMethodUserObject(method, project, editor)
                val methodNode = DefaultMutableTreeNode(testUserObject)
                classNode.add(methodNode)
            }
            (testListTree.model.root as DefaultMutableTreeNode).add(classNode)
        }

        val oldSelection = testListTree.selectionRows

        (testListTree.model as DefaultTreeModel).reload()
        TreeUtil.expandAll(testListTree)

        if (latestFile != null && psiFile == latestFile) {
            testListTree.selectionRows = oldSelection
        }

        latestFile = psiFile
    }

    /**
     * Determines whether this menu item is available for the current context.
     * Requires a project to be open.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    override fun update(e: AnActionEvent) {
        // Set the availability based on whether the project, psiFile and editor is not null
        e.presentation.isEnabled = (
            e.project != null &&
                FileEditorManager.getInstance(e.project!!).selectedEditor != null
            )
    }
}
