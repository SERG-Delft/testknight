package com.testbuddy

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project

/**
 * Implements the logic for highlighting all editors given an editor-highlighting function
 */
abstract class GlobalHighlighter(project: Project) {

    var highlighters = mutableListOf<RangeHighlighter>()
    private val fileEditorManager = FileEditorManager.getInstance(project)

    /**
     * Reconstruct all highlights
     */
    fun rebuildHighlights() {

        // remove all highlights
        removeHighlights()

        // highlight active editors
        fileEditorManager.allEditors.forEach {
            if (it is TextEditor) {
                highlightEditor(it.editor)
            }
        }
    }

    /**
     * Highlight the provided editors.
     *
     * @param editors the editors
     */
    fun addHighlights(editors: List<Editor>) {
        editors.forEach { highlightEditor(it) }
    }

    /**
     * Remove all Highlights
     */
    fun removeHighlights() {
        highlighters.forEach { it.dispose() }
    }

    /**
     * Highlight the lines covered within an editor.
     *
     * @param editor the editor
     */
    abstract fun highlightEditor(editor: Editor)
}
