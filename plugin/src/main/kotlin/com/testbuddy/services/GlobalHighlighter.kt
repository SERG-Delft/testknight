package com.testbuddy.services

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project

/**
 * Implements the logic for highlighting all editors given an editor-highlighting function
 */
abstract class GlobalHighlighter(project: Project) {

    /**
     * Keeps track of the
     */
    private var highlighters = mutableMapOf<Editor, MutableList<RangeHighlighter>>()
    private val fileEditorManager = FileEditorManager.getInstance(project)

    /**
     * Reconstruct all highlights
     */
    fun rebuildHighlights() {

        // remove all highlights
        removeHighlights()

        // get all open editors
        val editors = mutableListOf<Editor>()
        fileEditorManager.allEditors.forEach { if (it is TextEditor) editors.add(it.editor) }

        // highlight them
        addHighlights(editors)
    }

    /**
     * Highlight the provided editors.
     *
     * @param editors the editors
     */
    fun addHighlights(editors: List<Editor>) {
        editors.forEach {
            val newHls = highlightEditor(it)
            if (highlighters[it] == null) highlighters[it] = mutableListOf()
            highlighters[it]!!.addAll(newHls)
        }
    }

    /**
     * Remove all Highlights
     */
    fun removeHighlights() {

        // foreach editor, remove all of its highlights
        highlighters.keys.forEach { ed ->
            val hls = highlighters[ed]
            hls?.forEach { hl -> ed.markupModel.removeHighlighter(hl) }
        }
    }

    /**
     * Highlight the lines covered within an editor.
     *
     * @param editor the editor
     */
    abstract fun highlightEditor(editor: Editor): List<RangeHighlighter>
}
