package com.testbuddy.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.testbuddy.services.CoverageHighlighterService
import com.testbuddy.services.TestTracingService

class FileEditorListener(val project: Project) : FileEditorManagerListener {

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {

        val editors = mutableListOf<Editor>()
        source.getEditors(file).forEach { if (it is TextEditor) editors.add(it.editor) }

        project.service<TestTracingService>().addHighlights(editors)
        project.service<CoverageHighlighterService>().addHighlights(editors)
    }
}
