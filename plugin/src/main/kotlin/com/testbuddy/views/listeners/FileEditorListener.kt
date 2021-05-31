package com.testbuddy.com.testbuddy.views.listeners

import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.testbuddy.com.testbuddy.services.TestTracingService

class FileEditorListener(val project: Project) : FileEditorManagerListener {

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        project.service<TestTracingService>().refreshHighlights()
    }
}
