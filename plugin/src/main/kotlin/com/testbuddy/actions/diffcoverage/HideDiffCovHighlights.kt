package com.testbuddy.actions.diffcoverage

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.testbuddy.services.CoverageHighlighterService

class HideDiffCovHighlights : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.service<CoverageHighlighterService>()?.hideHighlights()
    }
}
