package com.testbuddy

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.testbuddy.services.DuplicateTestsService

class TestAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)
        val editor = event.getData(CommonDataKeys.EDITOR)
        val project = event.getData(CommonDataKeys.PROJECT)

        if (psiFile != null && editor != null && project != null) {

            val service = DuplicateTestsService(project)
            service.duplicateMethodUnderCaret(psiFile, editor)
        }
    }
}
