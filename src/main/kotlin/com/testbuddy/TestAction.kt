package com.testbuddy

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.branchingStatements.SwitchStatementChecklistNode
import com.testbuddy.services.TestMethodGenerationService

class TestAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)!!
        val editor = event.getData(CommonDataKeys.EDITOR)!!
        val project = event.getData(CommonDataKeys.PROJECT)!!

        val service = project.service<TestMethodGenerationService>()
        val checklistItem = SwitchStatementChecklistNode("This is the description", psiFile, "var", null)
        service.generateTestMethod(project, editor, checklistItem)
    }
}
