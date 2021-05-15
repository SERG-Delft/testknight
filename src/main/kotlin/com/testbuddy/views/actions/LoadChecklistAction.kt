package com.testbuddy.views.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBViewport
import com.intellij.ui.content.impl.ContentImpl
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import com.testbuddy.models.TestClassData
import com.testbuddy.models.TestMethodUserObject
import com.testbuddy.models.TestingChecklistLeafNode
import com.testbuddy.services.GenerateTestCaseChecklistService
import com.testbuddy.services.LoadTestsService
import javax.swing.JTabbedPane
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class LoadChecklistAction : AnAction() {

    /**
     * Updates the CheckList tab to add new checklist cases.
     *
     * @param event Event received when the associated menu item is chosen.
     */


    override fun actionPerformed(event: AnActionEvent) {

        val project = event.project ?: return
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)
        val editor = event.getData(CommonDataKeys.EDITOR)
        val psiClass = PsiTreeUtil.findChildOfType(psiFile, PsiClass::class.java)

        if (psiClass != null) {
            actionPerformed(project, psiClass, editor)
        }
    }

    /**
     * Updates the CopyPaste tab to load the test cases from the selected file.
     *
     * @param project current open project
     * @param psiFile PsiFile of the current open file
     * @param editor Editor of the current open file
     */
    fun actionPerformed(project: Project, psiClass: PsiClass, editor: Editor?) {



        val checklistService = project.service<GenerateTestCaseChecklistService>()
        val checklistClassTree = checklistService.generateClassChecklistFromClass(psiClass)
        val window: ToolWindow? = ToolWindowManager.getInstance(project).getToolWindow("TestBuddy")
        val tabbedPane = (
                (window!!.contentManager.contents[0] as ContentImpl)
                    .component as JTabbedPane
                )
        val checklistTab = tabbedPane.getComponentAt(1) as JBPanelWithEmptyText
        val checklistScroll = checklistTab.getComponent(1) as JBScrollPane
        val checklistViewport = checklistScroll.viewport
        val checklistTree = checklistViewport.getComponent(0) as Tree
        val root = checklistTree.model.root as DefaultMutableTreeNode
        //root.removeAllChildren()

//
//        val tabbedPane = (
//            (window!!.contentManager.contents[0] as ContentImpl)
//                .component as JTabbedPane
//            )
//        val copyPasteTab = tabbedPane.getComponentAt(1) as JBPanelWithEmptyText
//        val copyPasteScroll = copyPasteTab.getComponent(1) as JBScrollPane
//        val copyPasteViewport = copyPasteScroll.getComponent(0) as JBViewport
//        val checklistTree = copyPasteViewport.getComponent(0) as CheckboxTree
//
//        val checklistNode = DefaultMutableTreeNode("Checklist Method")
//        checklistNode.add(CheckedTreeNode("Checklist item x"))
//        (checklistTree.model.root as CheckedTreeNode).add(checklistNode)
//
//        // Reload updates the UI to have the new nodes
//        (checklistTree.model as DefaultTreeModel).reload()
//    }

        val classNode = DefaultMutableTreeNode(checklistClassTree)



        for (method in checklistClassTree.children) {

            val methodNode = DefaultMutableTreeNode(method)
            for (item in method.children){
                val itemNode = CheckedTreeNode(item)
                itemNode.isChecked = false
                methodNode.add(itemNode)
            }
            classNode.add(methodNode)
        }
        (checklistTree.model.root as CheckedTreeNode).add(classNode)
        (checklistTree.model as DefaultTreeModel).reload()
         TreeUtil.expandAll(checklistTree)
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
