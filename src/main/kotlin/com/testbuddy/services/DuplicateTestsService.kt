package com.testbuddy.services

import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import com.testbuddy.com.testbuddy.highlightResolutionStrategies.AssertionArgsStrategy
import com.testbuddy.com.testbuddy.highlightResolutionStrategies.ConstructorArgsStrategy
import com.testbuddy.com.testbuddy.highlightResolutionStrategies.MagicNumberStrategy

class DuplicateTestsService(project: Project) {

    private val templateCreationService = TemplateCreationService(project)
    private val templateManager = TemplateManager.getInstance(project)

    /**
     * List of active highlight resolution strategies
     */
    val highlightResolutionStrategies = listOf(
        AssertionArgsStrategy,
        ConstructorArgsStrategy,
        MagicNumberStrategy
    )

    /**
     * Gets a list of PSI elements to be highlighted ordered by priority of their resolution strategy
     */
    fun getHighlights(psiMethod: PsiMethod): List<PsiElement> {

        val highlights = mutableListOf<PsiElement>()
        highlightResolutionStrategies.forEach { highlights.addAll(it.getElements(psiMethod)) }

        if (highlights.size == 0) return highlights

        highlights.sortBy { it.endOffset }

        // interval scheduling algorithm, find largest subset of non-overlapping highlights
        val res = mutableListOf<PsiElement>()
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

        // prepare for template
        caret.moveToOffset(method.endOffset)
        editor.scrollingModel.scrollToCaret(ScrollType.CENTER)

        // run the template
        templateManager.startTemplate(editor, template)

        return true
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
