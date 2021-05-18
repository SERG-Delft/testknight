package com.testbuddy.services

import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset

class DuplicateTestsService(project: Project) {

    private val templateFactoryService = TemplateCreationService(project)
    private val templateManager = TemplateManager.getInstance(project)
    private val testAnalyzerService = TestAnalyzerService()

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
        val containingMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)

        if (containingMethod != null) {
            val template = templateFactoryService
                .createAdvancedTemplate(containingMethod, testAnalyzerService.getAssertionArgs(containingMethod))

            // prepare for template
            caret.moveToOffset(containingMethod.endOffset)
            editor.scrollingModel.scrollToCaret(ScrollType.CENTER)

            // run the template
            templateManager.startTemplate(editor, template)
        }
    }

    /**
     * Duplicates a test case in a given editor.
     *
     * @param method the PSI method to be duplicated
     * @param editor the text editor the method belongs to
     */
    fun duplicateMethod(method: PsiMethod, editor: Editor) {

        val caret = editor.caretModel.primaryCaret
        val template = templateFactoryService
            .createAdvancedTemplate(method, testAnalyzerService.getAssertionArgs(method))
        caret.moveToOffset(method.endOffset)
        editor.scrollingModel.scrollToCaret(ScrollType.CENTER)

        // run the template
        templateManager.startTemplate(editor, template)
    }
}
