package com.testknight.actions.testcases

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.testknight.services.DuplicateTestsService
import com.testknight.services.UsageDataService
import com.testknight.utilities.UserInterfaceHelper

class DuplicateTestUnderCaretAction : AnAction() {

    /**
     * Duplicates the test case under caret.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project!!
        val duplicateTestsService = project.service<DuplicateTestsService>()

        val psiFile = event.getData(CommonDataKeys.PSI_FILE)!!
        val editor = event.getData(CommonDataKeys.EDITOR)!!

        if (duplicateTestsService.duplicateMethodUnderCaret(psiFile, editor)) {
            UserInterfaceHelper.showTab(psiFile.project, "Test List")
        }
        UsageDataService.instance.recordDuplicateTest()
    }

    /**
     * Determines whether this menu item is available for the current context.
     * Requires a project to be open and psiFile and Editor to be accessible from the action event.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    override fun update(e: AnActionEvent) {
        // Set the availability based on whether the project, psiFile and editor is not null
        e.presentation.isEnabled = (
            e.project != null &&
                e.getData(CommonDataKeys.PSI_FILE) != null &&
                e.getData(CommonDataKeys.EDITOR) != null
            )
    }
}
