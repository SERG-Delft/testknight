package com.testbuddy.views.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.CheckboxTree
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.impl.ContentImpl
import com.testbuddy.models.TestingChecklistClassNode
import com.testbuddy.services.ChecklistTreeService
import com.testbuddy.services.GenerateTestCaseChecklistService
import javax.swing.JTabbedPane
import javax.swing.tree.DefaultMutableTreeNode

class LoadChecklistAction : AnAction() {

    /**
     * Updates the CheckList tab to add new checklist cases.
     *
     * @param event Event received when the associated menu item is chosen.
     */

    override fun actionPerformed(event: AnActionEvent) {

        val project = event.project!!
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)

        val psiClass = PsiTreeUtil.findChildOfType(psiFile, PsiClass::class.java) ?: return
        actionPerformed(project, psiClass)
    }

    /**
     * Updates the Checklist tab to load the test cases from the selected class.
     *
     * @param project current open project
     * @param psiElement PsiElement of the chosen element
     */
    fun actionPerformed(project: Project, psiElement: PsiElement) {

        val checklistService = project.service<GenerateTestCaseChecklistService>()
        var checklistClassTree: TestingChecklistClassNode? = null
        if (psiElement is PsiClass) {
            checklistClassTree = checklistService.generateClassChecklistFromClass(psiElement)
        } else if (psiElement is PsiMethod) {
            checklistClassTree = checklistService.generateClassChecklistFromMethod(psiElement)
        }
        val window: ToolWindow? = ToolWindowManager.getInstance(project).getToolWindow("TestBuddy")
        val tabbedPane = (
            (window!!.contentManager.contents[0] as ContentImpl)
                .component as JTabbedPane
            )
        val checklistTab = tabbedPane.getComponentAt(1) as JBPanelWithEmptyText
        val checklistScroll = checklistTab.getComponent(1) as JBScrollPane
        val checklistViewport = checklistScroll.viewport
        val checklistTree = checklistViewport.getComponent(0) as CheckboxTree
        val root = checklistTree.model.root as DefaultMutableTreeNode
        // root.removeAllChildren()

        val checklistTreeService = project.service<ChecklistTreeService>()
        if (checklistClassTree != null) {
            checklistTreeService.addChecklist(checklistClassTree)
            checklistTreeService.print()
        }

//        val classNode = CheckedTreeNode(ChecklistUserObject(checklistClassTree!!, 0))
//
//
//        // All userObjects start with check count 0
//        for (method in checklistClassTree.children) {
//
//            val methodNode = CheckedTreeNode(ChecklistUserObject(method, 0))
//
//            for (item in method.children) {
//                val itemNode = CheckedTreeNode(ChecklistUserObject(item, 0))
//                itemNode.isChecked = false
//                methodNode.add(itemNode)
//            }
//            classNode.add(methodNode)
//        }
//        (checklistTree.model.root as CheckedTreeNode).add(classNode)

        // version 2

//        val classNode = CheckedTreeNode(ChecklistNode(checklistClassTree!!.description,
        //        checklistClassTree!!.element, 0, false))
//
//
//        // All userObjects start with check count 0
//        for (method in checklistClassTree.children) {
//
//            val methodNode = CheckedTreeNode(ChecklistNode(method!!.description, method!!.element, 0, false))
//
//            for (item in method.children) {
//                val itemNode = CheckedTreeNode(ChecklistNode(item!!.description, item!!.element, 0, true))
//                itemNode.isChecked = false
//                methodNode.add(itemNode)
//            }
//            classNode.add(methodNode)
//        }
//        (checklistTree.model.root as CheckedTreeNode).add(classNode)
//
//        (checklistTree.model as DefaultTreeModel).reload()
//        TreeUtil.expandAll(checklistTree)
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
                e.getData(CommonDataKeys.PSI_FILE) != null
            )
    }
}
