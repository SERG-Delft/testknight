package com.testknight

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.testFramework.UsefulTestCase
import com.testknight.extensions.TestKnightTestCase
import com.testknight.services.GlobalHighlighter
import org.junit.Test

internal class GlobalHighlighterTest : TestKnightTestCase() {

    @Test
    fun testAddHl() {

        val fileEditorManager = FileEditorManager.getInstance(project)

        val highlighter = object : GlobalHighlighter(project) {
            override fun highlightEditor(editor: Editor): List<RangeHighlighter> = listOf(
                editor.markupModel.addLineHighlighter(5, 0, TextAttributes())
            )
        }

        myFixture.configureByFile("Person.java")
        myFixture.configureByFile("Dijkstra.java")

        val editors = mutableListOf<Editor>()
        fileEditorManager.allEditors.forEach { if (it is TextEditor) editors.add(it.editor) }

        highlighter.addHighlights(editors)

        editors.forEach { ed -> UsefulTestCase.assertSize(1, ed.markupModel.allHighlighters) }
    }

    @Test
    fun testRemoveHl() {

        val fileEditorManager = FileEditorManager.getInstance(project)

        val highlighter = object : GlobalHighlighter(project) {
            override fun highlightEditor(editor: Editor): List<RangeHighlighter> = listOf(
                editor.markupModel.addLineHighlighter(5, 0, TextAttributes())
            )
        }

        myFixture.configureByFile("Person.java")
        myFixture.configureByFile("Dijkstra.java")

        val editors = mutableListOf<Editor>()
        fileEditorManager.allEditors.forEach { if (it is TextEditor) editors.add(it.editor) }

        highlighter.addHighlights(editors)
        highlighter.removeHighlights()

        // since highlighters have been removed there should be zero hls on each edi
        editors.forEach { ed -> UsefulTestCase.assertSize(0, ed.markupModel.allHighlighters) }
    }

    @Test
    fun testRebuildHls() {

        val fileEditorManager = FileEditorManager.getInstance(project)

        val highlighter = object : GlobalHighlighter(project) {
            override fun highlightEditor(editor: Editor): List<RangeHighlighter> = listOf(
                editor.markupModel.addLineHighlighter(5, 0, TextAttributes())
            )
        }

        myFixture.configureByFile("Person.java")
        myFixture.configureByFile("Dijkstra.java")

        val editors = mutableListOf<Editor>()
        fileEditorManager.allEditors.forEach { if (it is TextEditor) editors.add(it.editor) }

        highlighter.addHighlights(editors)
        highlighter.removeHighlights()
        highlighter.rebuildHighlights()

        // since highlighters have been rebuilt there should be one hls on each editor
        editors.forEach { ed -> UsefulTestCase.assertSize(1, ed.markupModel.allHighlighters) }
    }
}
