package com.testbuddy.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.testbuddy.services.CoverageHighlighterService

class HideDiffCovHighlights : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return

        val coverageHighlighterService = project.service<CoverageHighlighterService>()

        coverageHighlighterService.hideHighlights(editor)
    }
}
