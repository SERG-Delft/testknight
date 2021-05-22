package com.testbuddy.services

import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.project.Project
import com.testbuddy.com.testbuddy.extensions.DiffCoverageLineMarkerRenderer
import java.awt.Color

@Suppress("MagicNumber")
class CoverageHighlighterService(val project: Project) {

    private val highlights = hashMapOf<Editor, MutableSet<RangeHighlighter>>()
    private val isHighlighted = hashMapOf<Editor, Boolean>()
    private val covDataService = project.service<CoverageDataService>()

    private val deletedColor = Color(237, 71, 71)
    private val includedColor = Color(64, 120, 68)

    /**
     * Display a all diff-coverage highlights in a given editor and class.
     *
     * @param editor the editor
     * @param className the class Name
     */
    fun showHighlights(editor: Editor, className: String) {

        covDataService.getDiffLines(project)
        val covDiffObject = covDataService.classCoveragesMap[className] ?: return

        for (line in covDiffObject.linesNewlyRemoved) {
            addGutterHighlighter(editor, line, deletedColor)
        }

        for (line in covDiffObject.linesNewlyAdded) {
            addGutterHighlighter(editor, line, includedColor)
        }
    }

    /**
     * Hide all diff-coverage highlights in the given editor and class.
     *
     * @param editor the editor
     */
    fun hideHighlights(editor: Editor) {
        highlights[editor]?.forEach { editor.markupModel.removeHighlighter(it) }
        isHighlighted[editor] = false
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
     */
    private fun addGutterHighlighter(editor: Editor, lineNum: Int, color: Color) {

        if (isHighlighted[editor] == true) return

        val hl = editor.markupModel.addLineHighlighter(
            null,
            lineNum - 1,
            HighlighterLayer.LAST
        )

        if (highlights[editor] == null) highlights[editor] = mutableSetOf()
        highlights[editor]!!.add(hl)
        isHighlighted[editor] = true

        hl.lineMarkerRenderer = DiffCoverageLineMarkerRenderer(color)
    }
}
