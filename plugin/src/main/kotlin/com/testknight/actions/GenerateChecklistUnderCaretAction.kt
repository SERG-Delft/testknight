package com.testknight.actions

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testknight.utilities.UserInterfaceHelper

class GenerateChecklistUnderCaretAction : AnAction() {

    /**
     * Updates the CheckList tab to add new checklist cases.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(event: AnActionEvent) {
        val file = event.getData(CommonDataKeys.PSI_FILE)!!
        val editor = event.getData(CommonDataKeys.EDITOR)!!
        val project = event.project!!

        val caret = editor.caretModel.primaryCaret
        val element = file.findElementAt(caret.offset)
        val containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
        val containingClass = PsiTreeUtil.getParentOfType(element, PsiClass::class.java)

        val checklistAction = ActionManager.getInstance().getAction("ChecklistAction") as LoadChecklistAction

        if (containingMethod != null && checklistAction.actionPerformed(project, containingMethod)) {
            UserInterfaceHelper.showTab(project, "Checklist")
        } else if (containingClass != null && checklistAction.actionPerformed(project, containingClass)) {
            UserInterfaceHelper.showTab(project, "Checklist")
        }
    }

    /**
     * Determines whether this menu item is available for the current context.
     * Requires a project to be open and psiFile and Editor to be accessible from the action event.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    override fun update(e: AnActionEvent) {
        // Set the availability based on whether psiFile and editor is not null
        e.presentation.isEnabled = (
            e.project != null &&
                e.getData(CommonDataKeys.PSI_FILE) != null &&
                e.getData(CommonDataKeys.EDITOR) != null
            )
    }
}
