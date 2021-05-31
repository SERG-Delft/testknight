package com.testbuddy.com.testbuddy.services

import com.intellij.openapi.components.service
import com.intellij.openapi.diff.DiffColors
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.project.Project
import com.intellij.ui.ColorUtil
import com.testbuddy.com.testbuddy.extensions.DiffCoverageLineMarkerRenderer
import com.testbuddy.com.testbuddy.settings.SettingsService
import java.awt.Color

class CoverageHighlighterService(val project: Project) {

    private val highlights = hashMapOf<Editor, MutableSet<RangeHighlighter>>()
    private val covDataService = project.service<CoverageDataService>()

    private fun settingsState() = SettingsService.instance.state

    private fun deletedColor() = ColorUtil.fromHex(settingsState().coverageSettings.deletedColor)
    private fun includedColor() = ColorUtil.fromHex(settingsState().coverageSettings.addedColor)

    /**
     * Display a all diff-coverage highlights in a given editor and class.
     *
     * @param editor the editor
     * @param className the class Name
     */
    private fun showHighlights(editor: Editor, className: String) {

        covDataService.getDiffLines(project)
        val covDiffObject = covDataService.classCoveragesMap[className] ?: return

        for (line in covDiffObject.linesNewlyRemoved) {
            addGutterHighlighter(editor, line, deletedColor())
        }

        for (line in covDiffObject.linesNewlyAdded) {
            addGutterHighlighter(editor, line, includedColor())
        }
    }

    /**
     * Applies the diff-coverage highlights to the editors to the diff view.
     *
     * @param leftEditor the left side editor for old coverage information
     * @param rightEditor the right side editor for new coverage information
     * @param className the class Name
     */
    fun showHighlightsInDiff(leftEditor: Editor, rightEditor: Editor, className: String) {

        covDataService.getDiffLines(project)
        val covDiffObject = covDataService.classCoveragesMap[className] ?: return

        for (line in covDiffObject.coveredPrev) {
            addGutterHighlighter(leftEditor, line, includedColor(), DiffColors.DIFF_INSERTED)
        }
        for (line in (covDiffObject.allLines - covDiffObject.coveredPrev)) {
            addGutterHighlighter(leftEditor, line, deletedColor(), DiffColors.DIFF_CONFLICT)
        }

        for (line in covDiffObject.coveredNow) {
            addGutterHighlighter(rightEditor, line, includedColor(), DiffColors.DIFF_INSERTED)
        }
        for (line in (covDiffObject.allLines - covDiffObject.coveredNow)) {
            addGutterHighlighter(rightEditor, line, deletedColor(), DiffColors.DIFF_CONFLICT)
        }
    }

    /**
     * Hide all diff-coverage highlights in the given editor and class.
     *
     * @param editor the editor
     */
    fun hideHighlights(editor: Editor) {
        highlights[editor]?.forEach { editor.markupModel.removeHighlighter(it) }
    }

    /**
     * Update the coverage diff highlights.
     *
     * @param editor the editor
     * @param className the class name
     */
    fun refreshHighlights(editor: Editor, className: String) {
        hideHighlights(editor)
        showHighlights(editor, className)
    }

    /**
     * Add a gutter highlight in an editor.
     *
     * @param editor the editor
     * @param lineNum the line number
     * @param color the color
     * @param attributeKey the text attribute key
     */
    private fun addGutterHighlighter(
        editor: Editor,
        lineNum: Int,
        color: Color,
        attributeKey: TextAttributesKey? = null
    ) {

        val hl = editor.markupModel.addLineHighlighter(
            attributeKey,
            lineNum - 1,
            HighlighterLayer.LAST
        )

        if (highlights[editor] == null) highlights[editor] = mutableSetOf()
        highlights[editor]!!.add(hl)

        hl.lineMarkerRenderer = DiffCoverageLineMarkerRenderer(color)
    }
}
