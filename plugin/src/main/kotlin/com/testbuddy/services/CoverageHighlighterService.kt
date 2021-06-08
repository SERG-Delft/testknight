package com.testbuddy.services

import com.intellij.openapi.components.service
import com.intellij.openapi.diff.DiffColors
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.ColorUtil
import com.testbuddy.extensions.DiffCoverageLineMarkerRenderer
import com.testbuddy.settings.SettingsService
import java.awt.Color

class CoverageHighlighterService(val project: Project) : GlobalHighlighter(project) {

    private val psiDocumentManager = PsiDocumentManager.getInstance(project)
    private var covDataService = project.service<CoverageDataService>()

    private fun deletedColor() = ColorUtil.fromHex(SettingsService.instance.state.coverageSettings.deletedColor)
    private fun includedColor() = ColorUtil.fromHex(SettingsService.instance.state.coverageSettings.addedColor)

    /**
     * Display a all diff-coverage highlights in a given editor and class.
     *
     * @param editor the editor
     */
    override fun highlightEditor(editor: Editor): List<RangeHighlighter> {

        val psiFile = psiDocumentManager.getPsiFile(editor.document)
        val classQn = PsiTreeUtil.findChildOfType(psiFile, PsiClass::class.java)?.qualifiedName

        covDataService.getDiffLines(project)
        covDataService.getDiffLines(project)
        val covDiffObject = covDataService.classCoveragesMap[classQn] ?: return listOf()

        val vFile = psiDocumentManager.getPsiFile(editor.document)?.virtualFile ?: return listOf()

        // if the editor was modified in between coverage runs skip
        if (vFile.modificationStamp != covDiffObject.currStamp ||
            covDiffObject.prevStamp != covDiffObject.currStamp
        ) return listOf()

        // collect the newly added highlighters
        val hls = mutableListOf<RangeHighlighter>()

        covDiffObject.linesNewlyAdded.forEach { hls.add(addGutterHighlighter(editor, it, includedColor())) }
        covDiffObject.linesNewlyRemoved.forEach { hls.add(addGutterHighlighter(editor, it, deletedColor())) }

        return hls
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
     * Add a gutter highlight in an editor.
     *
     * @param editor the editor
     * @param lineNum the line number
     * @param color the color
     * @param attributeKey the text attribute key
     */
    fun addGutterHighlighter(
        editor: Editor,
        lineNum: Int,
        color: Color,
        attributeKey: TextAttributesKey? = null
    ): RangeHighlighter {

        val hl = editor.markupModel.addLineHighlighter(
            attributeKey,
            lineNum - 1,
            HighlighterLayer.LAST
        )

        hl.lineMarkerRenderer = DiffCoverageLineMarkerRenderer(color)

        return hl
    }
}
