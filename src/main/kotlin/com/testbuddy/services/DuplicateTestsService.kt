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

class DuplicateTestsService(project: Project) {

    private val templateCreationService = TemplateCreationService(project)
    private val templateManager = TemplateManager.getInstance(project)

    /**
     * List of active highlight resolution strategies
     */
    private val highlightResolutionStrategies = listOf(AssertionArgsStrategy, ConstructorArgsStrategy)

    /**
     * Gets a list of PSI elements to be highlighted ordered by priority of their resolution strategy
     */
    private fun getHighlights(psiMethod: PsiMethod): List<PsiElement> {

        val highlights = mutableListOf<PsiElement>()

        highlightResolutionStrategies
            .forEach { highlights.addAll(it.getElements(psiMethod)) }

        return highlights.sortedBy { it.startOffset }
    }

    /**
     * Duplicates the test case method under the caret.
     *
     * @param file the PSI file.
     * @param editor represents the current instance of a text editor.
     */
    fun duplicateMethodUnderCaret(file: PsiFile, editor: Editor) {

        val caret = editor.caretModel.primaryCaret
        val offset = caret.offset
        val element = file.findElementAt(offset)
        val method = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java) ?: return

        val template = templateCreationService.createAdvancedTemplate(method, getHighlights(method))

        // prepare for template
        caret.moveToOffset(method.endOffset)
        editor.scrollingModel.scrollToCaret(ScrollType.CENTER)

        // run the template
        templateManager.startTemplate(editor, template)
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
