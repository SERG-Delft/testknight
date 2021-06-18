package com.testknight.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.testknight.services.TestTracingService

class HideTracedTestHighlightsAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val testTracingService = project.service<TestTracingService>()

        testTracingService.removeHighlights()
    }
}
