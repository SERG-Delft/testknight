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
import com.intellij.ui.CheckboxTreeHelper
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.impl.ContentImpl
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import com.testbuddy.models.TestingChecklistClassNode
import com.testbuddy.models.TestingChecklistNode
import com.testbuddy.services.GenerateTestCaseChecklistService
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

        val psiClass = PsiTreeUtil.findChildOfType(psiFile, PsiClass::class.java)
        actionPerformed(project, psiClass as PsiElement)
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
        val checklistTree = checklistViewport.getComponent(0) as Tree
        val root = checklistTree.model.root as DefaultMutableTreeNode
        root.removeAllChildren()
        val list = CheckboxTreeHelper.getCheckedNodes(CheckedTreeNode::class.java, null, checklistTree!!.model)
        println(list.size)

        val classNode = CheckedTreeNode(checklistClassTree)

        for (method in checklistClassTree!!.children) {

            val methodNode = CheckedTreeNode(method)
            val x = Tree.NodeFilter<TestingChecklistNode> { true }
            println(
                "CHECKLIST ITEM: ${CheckboxTreeHelper.getCheckedNodes<TestingChecklistNode>
                (TestingChecklistNode::class.java, x, checklistTree.model).size}"
            )
            for (item in method.children) {
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
