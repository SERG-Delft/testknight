package com.testbuddy.views.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.models.TestingChecklistClassNode
import com.testbuddy.services.ChecklistTreeService
import com.testbuddy.services.GenerateTestCaseChecklistService

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
        actionPerformed(project, psiClass, true)
    }

    /**
     * Updates the Checklist tab to load the test cases from the selected class.
     *
     * @param project current open project
     * @param psiElement PsiElement of the chosen element
     */
    fun actionPerformed(project: Project, psiElement: PsiElement, refresh: Boolean = false) {

        val checklistService = project.service<GenerateTestCaseChecklistService>()
        var checklistClassTree: TestingChecklistClassNode? = null
        if (psiElement is PsiClass) {
            checklistClassTree = checklistService.generateClassChecklistFromClass(psiElement)
        } else if (psiElement is PsiMethod) {
            checklistClassTree = checklistService.generateClassChecklistFromMethod(psiElement)
        }

        val checklistTreeService = project.service<ChecklistTreeService>()
        if (refresh) {
            checklistTreeService.resetTree()
        }

        if (checklistClassTree != null) {
            checklistTreeService.addChecklist(checklistClassTree)
        }
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
