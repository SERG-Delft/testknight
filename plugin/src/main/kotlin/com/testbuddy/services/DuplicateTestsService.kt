package com.testbuddy.services

import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import com.testbuddy.highlightResolutionStrategies.AssertionArgsStrategy
import com.testbuddy.highlightResolutionStrategies.ConstructorArgsStrategy
import com.testbuddy.highlightResolutionStrategies.MagicNumberStrategy
import com.testbuddy.models.HighlightedTextData

class DuplicateTestsService(project: Project) {

    private val templateCreationService = project.service<TemplateCreationService>()
    private val templateManager = TemplateManager.getInstance(project)
    private val testAnalyzerService = project.service<TestAnalyzerService>()

    /**
     * List of highlight resolution strategies
     */
    private val highlightResolutionStrategies = listOf(
        AssertionArgsStrategy,
        ConstructorArgsStrategy,
        MagicNumberStrategy
    )

    /**
     * Gets a list of PSI elements to be highlighted ordered by priority of their resolution strategy
     */
    fun getHighlights(psiMethod: PsiMethod): List<HighlightedTextData> {

        val highlights = mutableListOf<HighlightedTextData>()
        highlightResolutionStrategies.forEach { if (it.isEnabled()) highlights.addAll(it.getElements(psiMethod)) }

        if (highlights.size == 0) return highlights

        highlights.sortBy { it.endOffset }

        // interval scheduling algorithm, find largest subset of non-overlapping highlights
        val res = mutableListOf<HighlightedTextData>()
        res.add(highlights[0])
        var lastAdded = highlights[0]

        for (hl in highlights) {

            if (hl.startOffset >= lastAdded.endOffset) {
                res.add(hl)
                lastAdded = hl
            }
        }

        return res
    }

    /**
     * Duplicates the test case method under the caret.
     *
     * @param file the PSI file.
     * @param editor represents the current instance of a text editor.
     */
    fun duplicateMethodUnderCaret(file: PsiFile, editor: Editor): Boolean {
        val caret = editor.caretModel.primaryCaret
        val offset = caret.offset
        val element = file.findElementAt(offset)
        val method = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java) ?: return false

        val template = templateCreationService.createAdvancedTemplate(method, getHighlights(method))

        editor.document
        // prepare for template
        caret.moveToOffset(method.endOffset)
        editor.scrollingModel.scrollToCaret(ScrollType.CENTER)

        // run the template
        templateManager.startTemplate(editor, template)

        return testAnalyzerService.isTestMethod(method)
    }

    /**
     * Duplicates a test case in a given editor.
     *
     * @param method the PSI method to be duplicated
     * @param editor the text editor the method belongs to
     */
    fun duplicateMethod(method: PsiMethod, editor: Editor) {

        val caret = editor.caretModel.primaryCaret
        val template = templateCreationService.createAdvancedTemplate(method, getHighlights(method))

        // prepare for template
        caret.moveToOffset(method.endOffset)
        editor.scrollingModel.scrollToCaret(ScrollType.CENTER)

        // run the template
        templateManager.startTemplate(editor, template)
    }
}
