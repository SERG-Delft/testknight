package com.testbuddy.services

import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.MarkupModel
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.rt.coverage.data.ClassData
import com.intellij.rt.coverage.data.LineData
import com.intellij.rt.coverage.data.ProjectData
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import com.testbuddy.extensions.DiffCoverageLineMarkerRenderer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import java.awt.Color

class CoverageHighlighterServiceTest : LightJavaCodeInsightFixtureTestCase() {

    override fun getTestDataPath() = "testdata"

    @Test
    fun testAddGutterHighlighter() {
        val service = CoverageHighlighterService(project)
        val editor = mockk<Editor>()
        val markup = mockk<MarkupModel>()
        every { editor.markupModel } returns markup

        val rangeHighlighter = mockk<RangeHighlighter>()

        every { rangeHighlighter.lineMarkerRenderer = any() } returns Unit
        every { markup.addLineHighlighter(null, 1, 6000) } returns rangeHighlighter

        service.addGutterHighlighter(editor, 2, Color.white)

        verify { rangeHighlighter.lineMarkerRenderer = any() }
    }

    @Test
    fun testHighlightEditor() {

        myFixture.configureByFile("Point.java")
        val editor = myFixture.editor
        val document = editor.document

        // old coverage data
        val pointPrevLines = Array(21) { i -> LineData(i, "") }

        // set covered and uncovered lines (all but 6..9 and 11..14 are uncovered)
        for (line in pointPrevLines) line.hits = 0
        for (i in 6..9) pointPrevLines[i].hits = 1
        for (i in 11..14) pointPrevLines[i].hits = 1

        // new coverage data
        val pointNewLines = Array(21) { i -> LineData(i, "") }

        // now 6..9 is no longer covered but 16..19 are
        for (line in pointPrevLines) line.hits = 0
        for (i in 6..9) pointPrevLines[i].hits = 1
        for (i in 16..19) pointNewLines[i].hits = 1

        // expected outcome:
        // Lines 11..14 are green
        // Lines 6..9 are red

        // set up prev and new class data
        val prevClassData = ClassData("Point"); prevClassData.setLines(pointPrevLines)
        val newClassData = ClassData("Point"); newClassData.setLines(pointNewLines)

        // set up projectData mocks
        val prevData = mockk<ProjectData>(); every { prevData.classes } returns mapOf("Point" to prevClassData)
        val newData = mockk<ProjectData>(); every { newData.classes } returns mapOf("Point" to newClassData)

        val covDataService = project.service<CoverageDataService>()

        covDataService.updateCoverage(null, prevData)
        covDataService.updateCoverage(null, newData)

        covDataService.getDiffLines()

        // highlight the editor
        project.service<CoverageHighlighterService>().highlightEditor(myFixture.editor)

        val highlights = editor.markupModel
            .allHighlighters
            .map {
                val color = (it.lineMarkerRenderer as DiffCoverageLineMarkerRenderer).color
                val line = document.getLineNumber(it.startOffset) + 1
                Pair(line, color)
            }

        val green = Color(0, 255, 0)
        val red = Color(255, 0, 0)

        assertContainsElements(highlights, Pair(16, green), Pair(17, green), Pair(18, green), Pair(19, green))
        assertContainsElements(highlights, Pair(6, red), Pair(7, red), Pair(8, red), Pair(9, red))
    }
}
