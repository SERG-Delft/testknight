package com.testbuddy

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.testbuddy.services.DuplicateTestsService

class TestAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val aaa = DuplicateTestsService()
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)
        val editor = event.getData(CommonDataKeys.EDITOR)
        val project = event.getData(CommonDataKeys.PROJECT)
        // val document = event.getData(CommonDataKeys.EDITOR).getDocument()

        if (psiFile != null && editor != null && project != null) {
            aaa.addToPsi(psiFile, editor, project)
        }
    }
}
