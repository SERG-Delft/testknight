package com.testbuddy.services

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.MarkupModel
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.testbuddy.extensions.TestBuddyTestCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test
import java.awt.Color

class CoverageHighlighterServiceTest : TestBuddyTestCase() {

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

//    @Test
//    fun testRebuildHighlights() {
//        myFixture.configureByFile("/Math.java")
//        val service = spyk(CoverageHighlighterService(project))
//
//        service.rebuildHighlights()
//
//        verify { service.hideHighlights() }
//        verify { service.addHighlights(any()) }
//    }
//
//    @Test
//    fun testHideHighlights() {
//        myFixture.configureByFile("/Math.java")
//        val editor = myFixture.editor
//
//        val service = CoverageHighlighterService(project)
//        val highlighter = editor.markupModel.addRangeHighlighter(0, 1, 12, TextAttributes(), HighlighterTargetArea.EXACT_RANGE)
//        val highlighterSpy = spyk(highlighter)
//
//        service.setHighlights(mutableListOf(highlighterSpy))
//        service.hideHighlights()
//
//        verify { highlighterSpy.dispose() }
//    }

//    @Test
//    fun testHideHighlightsEmptyMapForeachNotRun() {
//        myFixture.configureByFile("/Methods.java")
//
//        val editor = mockk<Editor>()
//        val service = CoverageHighlighterService(project)
//        val rangeHighlighter = mockk<RangeHighlighter>()
//        val mutableHashMap = HashMap<Editor, MutableSet<RangeHighlighter>>()
//        val markupModel = mockk<MarkupModel>()
//
//        mutableHashMap[editor] = mutableSetOf(rangeHighlighter)
//        service.setHighlights(mutableHashMap)
//
//        every { editor.markupModel } returns markupModel
//        every { markupModel.removeHighlighter(any()) } returns Unit
//        // a different editor is passed
//        service.hideHighlights(myFixture.editor)
//        verify(inverse = true) { editor.markupModel }
//    }
//
//    @Test
//    fun testShowHighlightsNull() {
//        myFixture.configureByFile("/Methods.java")
//        val service = CoverageHighlighterService(project)
//        val covService = mockk<CoverageDataService>()
//        service.setCoverageDataService(covService)
//        every { covService.getDiffLines(any()) } returns Unit
//        every { covService.classCoveragesMap[any()] } returns null
//
//        service.showHighlights(myFixture.editor, "ClassName")
//
//        TestCase.assertEquals(covService.classCoveragesMap["ClassName"], null)
//    }
//
//    @Test
//    fun testShowHighlights() {
//        myFixture.configureByFile("/Methods.java")
//        val service = spyk(CoverageHighlighterService(project))
//        val covService = mockk<CoverageDataService>()
//        service.setCoverageDataService(covService)
//        every { covService.getDiffLines(any()) } returns Unit
//
//        val allLines = setOf(1, 2, 3)
//        val coveredPrev = setOf(1, 2)
//        val coveredNow = setOf(2, 3)
//        val coverageDiffObject = CoverageDiffObject(allLines, coveredPrev, coveredNow)
//
//        val mutableMap = mutableMapOf<String, CoverageDiffObject>()
//        mutableMap.put("ClassName", coverageDiffObject)
//
//        every { covService.classCoveragesMap } returns mutableMap
//        println(mutableMap["ClassName"]!!.linesNewlyRemoved)
//        service.showHighlights(myFixture.editor, "ClassName")
//        verify { service.addGutterHighlighter(any(), any(), any()) }
//    }
//
//    @Test
//    fun testShowHighlightsInDiffCoveredPrev() {
//        myFixture.configureByFile("/Math.java")
//        val service = spyk(CoverageHighlighterService(project))
//        val covService = mockk<CoverageDataService>()
//        service.setCoverageDataService(covService)
//        every { covService.getDiffLines(any()) } returns Unit
//
//        val allLines = setOf(1, 2, 3, 4, 5)
//        val coveredPrev = setOf(1, 2, 3, 4, 5)
//        val coveredNow = emptySet<Int>()
//        val coverageDiffObject = CoverageDiffObject(allLines, coveredPrev, coveredNow)
//
//        val mutableMap = mutableMapOf<String, CoverageDiffObject>()
//        mutableMap.put("ClassName", coverageDiffObject)
//
//        every { covService.classCoveragesMap } returns mutableMap
//        every { service.addGutterHighlighter(any(), any(), any(), any()) } returns Unit
//        service.showHighlightsInDiff(myFixture.editor, myFixture.editor, "ClassName")
//
//        verify { service.addGutterHighlighter(any(), 1, any(), DiffColors.DIFF_INSERTED) }
//        verify { service.addGutterHighlighter(any(), 2, any(), DiffColors.DIFF_INSERTED) }
//        verify { service.addGutterHighlighter(any(), 3, any(), DiffColors.DIFF_INSERTED) }
//        verify { service.addGutterHighlighter(any(), 4, any(), DiffColors.DIFF_INSERTED) }
//        verify { service.addGutterHighlighter(any(), 5, any(), DiffColors.DIFF_INSERTED) }
//    }
//
//    @Test
//    fun testShowHighlightsInDiffAllButCoveredPrev() {
//        myFixture.configureByFile("/Math.java")
//        val service = spyk(CoverageHighlighterService(project))
//        val covService = mockk<CoverageDataService>()
//        service.setCoverageDataService(covService)
//        every { covService.getDiffLines(any()) } returns Unit
//
//        val allLines = setOf(1, 2, 3, 4, 5)
//        val coveredPrev = setOf(4)
//        val coveredNow = emptySet<Int>()
//        val coverageDiffObject = CoverageDiffObject(allLines, coveredPrev, coveredNow)
//
//        val mutableMap = mutableMapOf<String, CoverageDiffObject>()
//        mutableMap.put("ClassName", coverageDiffObject)
//
//        every { covService.classCoveragesMap } returns mutableMap
//        every { service.addGutterHighlighter(any(), any(), any(), any()) } returns Unit
//        service.showHighlightsInDiff(myFixture.editor, myFixture.editor, "ClassName")
//
//        verify { service.addGutterHighlighter(any(), 1, any(), DiffColors.DIFF_CONFLICT) }
//        verify { service.addGutterHighlighter(any(), 2, any(), DiffColors.DIFF_CONFLICT) }
//        verify { service.addGutterHighlighter(any(), 3, any(), DiffColors.DIFF_CONFLICT) }
//        verify { service.addGutterHighlighter(any(), 5, any(), DiffColors.DIFF_CONFLICT) }
//    }
//
//    @Test
//    fun testShowHighlightsInDiffCoveredNow() {
//        myFixture.configureByFile("/Math.java")
//        val service = spyk(CoverageHighlighterService(project))
//        val covService = mockk<CoverageDataService>()
//        service.setCoverageDataService(covService)
//        every { covService.getDiffLines(any()) } returns Unit
//
//        val allLines = setOf(1, 2, 3, 4, 5)
//        val coveredPrev = emptySet<Int>()
//        val coveredNow = setOf(1, 2, 4, 5)
//        val coverageDiffObject = CoverageDiffObject(allLines, coveredPrev, coveredNow)
//
//        val mutableMap = mutableMapOf<String, CoverageDiffObject>()
//        mutableMap.put("ClassName", coverageDiffObject)
//
//        every { covService.classCoveragesMap } returns mutableMap
//        every { service.addGutterHighlighter(any(), any(), any(), any()) } returns Unit
//        service.showHighlightsInDiff(myFixture.editor, myFixture.editor, "ClassName")
//
//        verify { service.addGutterHighlighter(any(), 1, any(), DiffColors.DIFF_INSERTED) }
//        verify { service.addGutterHighlighter(any(), 2, any(), DiffColors.DIFF_INSERTED) }
//        verify { service.addGutterHighlighter(any(), 4, any(), DiffColors.DIFF_INSERTED) }
//        verify { service.addGutterHighlighter(any(), 5, any(), DiffColors.DIFF_INSERTED) }
//    }
//
//    @Test
//    fun testShowHighlightsInDiffAllButCoveredNow() {
//        myFixture.configureByFile("/Math.java")
//        val service = spyk(CoverageHighlighterService(project))
//        val covService = mockk<CoverageDataService>()
//        service.setCoverageDataService(covService)
//        every { covService.getDiffLines(any()) } returns Unit
//
//        val allLines = setOf(1, 2, 3, 4, 5)
//        val coveredPrev = emptySet<Int>()
//        val coveredNow = setOf(2)
//        val coverageDiffObject = CoverageDiffObject(allLines, coveredPrev, coveredNow)
//
//        val mutableMap = mutableMapOf<String, CoverageDiffObject>()
//        mutableMap.put("ClassName", coverageDiffObject)
//
//        every { covService.classCoveragesMap } returns mutableMap
//        every { service.addGutterHighlighter(any(), any(), any(), any()) } returns Unit
//        service.showHighlightsInDiff(myFixture.editor, myFixture.editor, "ClassName")
//
//        verify { service.addGutterHighlighter(any(), 1, any(), DiffColors.DIFF_CONFLICT) }
//        verify { service.addGutterHighlighter(any(), 3, any(), DiffColors.DIFF_CONFLICT) }
//        verify { service.addGutterHighlighter(any(), 4, any(), DiffColors.DIFF_CONFLICT) }
//        verify { service.addGutterHighlighter(any(), 5, any(), DiffColors.DIFF_CONFLICT) }
//    }
//
//    @Test
//    fun testClassCoveragesMapReturnNullDiff() {
//        myFixture.configureByFile("/Math.java")
//        val service = spyk(CoverageHighlighterService(project))
//        val covService = mockk<CoverageDataService>()
//        service.setCoverageDataService(covService)
//        every { covService.getDiffLines(any()) } returns Unit
//
//        val allLines = setOf(1, 2, 3, 4, 5)
//        val coveredPrev = emptySet<Int>()
//        val coveredNow = setOf(1, 2, 4, 5)
//        val coverageDiffObject = CoverageDiffObject(allLines, coveredPrev, coveredNow)
//
//        val mutableMap = mutableMapOf<String, CoverageDiffObject>()
//        mutableMap.put("PreviousClassName", coverageDiffObject)
//
//        every { covService.classCoveragesMap } returns mutableMap
//        every { service.addGutterHighlighter(any(), any(), any(), any()) } returns Unit
//        service.showHighlightsInDiff(myFixture.editor, myFixture.editor, "ADifferentClassName")
//
//        // verify that no calls to addGutterHighlighter were made
//        verify(inverse = true) { service.addGutterHighlighter(any(), any(), any(), any()) }
//    }
}
