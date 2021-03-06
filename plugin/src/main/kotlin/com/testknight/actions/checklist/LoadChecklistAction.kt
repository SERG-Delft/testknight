package com.testknight.actions.checklist

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testknight.services.TestAnalyzerService
import com.testknight.services.UsageDataService
import com.testknight.services.checklist.ChecklistTreeService
import com.testknight.services.checklist.GenerateTestCaseChecklistService

class LoadChecklistAction : AnAction() {

    /**
     * Updates the CheckList tab to add new checklist cases.
     *
     * @param event Event received when the associated menu item is chosen.
     */

    override fun actionPerformed(event: AnActionEvent) {

        val project = event.project!!
        val textEditor = (FileEditorManager.getInstance(project).selectedEditor as TextEditor)
        val psiFile = PsiManager.getInstance(project).findFile(textEditor.file!!) ?: return

        val psiClass = PsiTreeUtil.findChildOfType(psiFile, PsiClass::class.java) ?: return
        actionPerformed(project, psiClass, false)
    }

    /**
     * Updates the Checklist tab to load the test cases from the selected class.
     *
     * @param project current open project
     * @param psiElement PsiElement of the chosen element
     *
     * @return True if the UI got updated. False otherwise.
     */

    fun actionPerformed(project: Project, psiElement: PsiElement, refresh: Boolean = false): Boolean {

        val checklistService = ApplicationManager
            .getApplication()
            .getService(GenerateTestCaseChecklistService::class.java)

        val checkTestService = TestAnalyzerService()
        var checklistClassTree: TestingChecklistClassNode? = null
        if (psiElement is PsiClass && !checkTestService.isTestClass(psiElement)) {
            checklistClassTree = checklistService.generateClassChecklistFromClass(psiElement)
        } else if (psiElement is PsiMethod && !checkTestService.isTestMethod(psiElement)) {
            checklistClassTree = checklistService.generateClassChecklistFromMethod(psiElement)
        }

        val checklistTreeService = project.service<ChecklistTreeService>()
        if (refresh) {
            checklistTreeService.resetTree()
        }

        if (checklistClassTree != null) {
            checklistTreeService.addChecklist(checklistClassTree)
            UsageDataService.instance.recordGenerateChecklist()
        }

        return true
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
