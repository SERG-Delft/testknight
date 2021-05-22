package com.testbuddy

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.testbuddy.com.testbuddy.extensions.DiffCoverageLineMarkerRenderer
import com.testbuddy.services.DuplicateTestsService

class TestAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)
        val editor = event.getData(CommonDataKeys.EDITOR)!!
        val project = event.getData(CommonDataKeys.PROJECT)!!

        val highligter = editor.markupModel.addRangeHighlighter(
            null,
            50,
            500,
            HighlighterLayer.SELECTION - 200,
            HighlighterTargetArea.EXACT_RANGE
        )

        highligter.lineMarkerRenderer = DiffCoverageLineMarkerRenderer(project)

    }
}
