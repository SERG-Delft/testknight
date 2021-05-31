package com.testbuddy.services

import com.intellij.codeInsight.template.TemplateManager
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.project.Project
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

class TestMethodGenerationService {

    /**
     * Generates and appends a test method for the given checklist item
     * in the current caret position.
     *
     * @param project the current project.
     * @param editor the current editor.
     * @param checklistItem the checklist item with to generate the method for.
     */
    fun generateTestMethod(project: Project, editor: Editor, checklistItem: TestingChecklistLeafNode) {
        val templateCreationService = project.service<TemplateCreationService>()
        val testMethod = checklistItem.generateTestMethod(project)
        val template = templateCreationService.createBasicTemplate(testMethod)
        editor.scrollingModel.scrollToCaret(ScrollType.CENTER)
        TemplateManager.getInstance(project).startTemplate(editor, template)
    }
}
