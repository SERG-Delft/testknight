package com.testbuddy.services

import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.MarkupModel
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.rt.coverage.data.ClassData
import com.intellij.rt.coverage.data.LineData
import com.intellij.rt.coverage.data.ProjectData
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import com.intellij.ui.ColorUtil
import com.testbuddy.extensions.DiffCoverageLineMarkerRenderer
import com.testbuddy.settings.SettingsService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
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

        // get which lines have been highlighted
        val highlights = mutableMapOf<Int, Color>()
        editor.markupModel
            .allHighlighters
            .forEach {
                val color = (it.lineMarkerRenderer as DiffCoverageLineMarkerRenderer).color
                val line = document.getLineNumber(it.startOffset) + 1
                highlights[line] = color
            }

        val red = ColorUtil.fromHex(SettingsService.instance.state.coverageSettings.deletedColor)
        val green = ColorUtil.fromHex(SettingsService.instance.state.coverageSettings.addedColor)

        // the expected behavior is that lines 6..8 are no longer covered
        // and lines 16..19 are newly covered
        val expected = mapOf(
            6 to red,
            7 to red,
            8 to red,
            9 to red,
            16 to green,
            17 to green,
            18 to green,
            19 to green
        )

        TestCase.assertEquals(expected, highlights)
    }
}
