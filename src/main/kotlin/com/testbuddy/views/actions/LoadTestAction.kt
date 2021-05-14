package com.testbuddy.views.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiFile
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.impl.ContentImpl
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import com.testbuddy.models.TestClassData
import com.testbuddy.models.TestMethodUserObject
import com.testbuddy.services.LoadTestsService
import javax.swing.JTabbedPane
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class LoadTestAction : AnAction() {

    /**
     * Updates the CopyPaste tab to load the test cases from the selected file.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(event: AnActionEvent) {

        val project = event.project ?: return
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)
        val editor = event.getData(CommonDataKeys.EDITOR)

        actionPerformed(project, psiFile, editor)
    }

    /**
     * Updates the CopyPaste tab to load the test cases from the selected file.
     *
     * @param project current open project
     * @param psiFile PsiFile of the current open file
     * @param editor Editor of the current open file
     */
    fun actionPerformed(project: Project, psiFile: PsiFile?, editor: Editor?) {

        val loadTestsService = project.service<LoadTestsService>()
        val listClasses = if (psiFile != null) loadTestsService.getTestsTree(psiFile) else emptyList<TestClassData>()

        val window: ToolWindow? = ToolWindowManager.getInstance(project).getToolWindow("TestBuddy")

        val tabbedPane = (
            (window!!.contentManager.contents[0] as ContentImpl)
                .component as JTabbedPane
            )
        val copyPasteTab = tabbedPane.getComponentAt(0) as JBPanelWithEmptyText
        val copyPasteScroll = copyPasteTab.getComponent(1) as JBScrollPane
        val copyPasteViewport = copyPasteScroll.viewport
        val copyPasteTree = copyPasteViewport.getComponent(0) as Tree
        val root = copyPasteTree.model.root as DefaultMutableTreeNode
        root.removeAllChildren()

        for (testClass in listClasses) {

            val classNode = DefaultMutableTreeNode(testClass)
            for (method in testClass.methods) {

                val testUserObject = TestMethodUserObject(method, project, editor)
                val methodNode = DefaultMutableTreeNode(testUserObject)
                classNode.add(methodNode)
            }
            (copyPasteTree.model.root as DefaultMutableTreeNode).add(classNode)
        }

        (copyPasteTree.model as DefaultTreeModel).reload()
        TreeUtil.expandAll(copyPasteTree)
    }

    /**
     * Determines whether this menu item is available for the current context.
     * Requires a project to be open.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    override fun update(e: AnActionEvent) {
        // Set the availability based on whether a project is open
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }
}
