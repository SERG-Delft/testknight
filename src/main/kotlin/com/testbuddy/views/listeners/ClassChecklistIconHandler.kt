package com.testbuddy.com.testbuddy.views.listeners

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.impl.ContentImpl
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import com.testbuddy.services.GenerateTestCaseChecklistService
import java.awt.event.MouseEvent
import javax.swing.JTabbedPane
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class ClassChecklistIconHandler : GutterIconNavigationHandler<PsiElement> {

    override fun navigate(event: MouseEvent?, elemnt: PsiElement?) {

        if (elemnt == null) return

        val project = elemnt.project

        val psiClass = (elemnt.parent as PsiClass)
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
        // root.removeAllChildren()

        val classNode = DefaultMutableTreeNode(checklistClassTree)

        for (method in checklistClassTree.children) {

            val methodNode = DefaultMutableTreeNode(method)
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
}
