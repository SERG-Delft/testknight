package com.testbuddy.services

import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

class TestMethodGenerationService {

    /**
     * Generates and appends a test method for the given checklist item
     * in the current caret position.
     *
     * @param file the fule currently open.
     * @param project the current project.
     * @param editor the current editor.
     * @param checklistItem the checklist item with to generate the method for.
     */
    fun generateTestMethod(project: Project, editor: Editor, checklistItem: TestingChecklistLeafNode) {
        val templateCreationService = project.service<TemplateCreationService>()
        val testMethod = checklistItem.generateTestMethod(project)
        val caret = editor.caretModel.primaryCaret
        val offset = caret.offset

        val textEditor = FileEditorManager.getInstance(project).selectedEditor as TextEditor? ?: return
        val file = PsiManager.getInstance(project).findFile(textEditor.file!!) ?: return

        val element = file.findElementAt(offset)
        val parentMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
        if (parentMethod != null) {
            caret.moveToOffset(parentMethod.endOffset)
        }
        val template = templateCreationService.createBasicTemplate(testMethod)
        editor.scrollingModel.scrollToCaret(ScrollType.CENTER)
        TemplateManager.getInstance(project).startTemplate(editor, template)
    }
}
