package com.testbuddy.services

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.MarkupModel
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.testbuddy.extensions.TestBuddyTestCase
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.spyk
import io.mockk.verify
import junit.framework.TestCase
import org.junit.Test
import java.awt.Color

class CoverageHighlighterServiceTest : TestBuddyTestCase() {


    @Test
    fun testAddGutterHighlighter(){
        val service = CoverageHighlighterService(project)
        val editor = mockk<Editor>()
        val markup = mockk<MarkupModel>()
        every { editor.markupModel } returns markup

        val rangeHighlighter = mockk<RangeHighlighter>()

        every{rangeHighlighter.setLineMarkerRenderer(any())} returns Unit
        every { markup.addLineHighlighter(null, 1, 6000) } returns rangeHighlighter

        service.addGutterHighlighter(editor, 2, Color.white)

        verify { rangeHighlighter.setLineMarkerRenderer(any()) }
    }

    @Test
    fun testRefreshHighlights(){
        val service = CoverageHighlighterService(project)
        val editor = mockk<Editor>()

        service.refreshHighlights(editor, "ClassName")

        verify {service.hideHighlights(editor)}
    }

    @Test
    fun testHideHighlights(){
        myFixture.configureByFile("/Math.java")

        val editor = mockk<Editor>()
        val service = CoverageHighlighterService(project)
        val rangeHighlighter = mockk<RangeHighlighter>()
        val mutableHashMap = HashMap<Editor, MutableSet<RangeHighlighter>>()
        val markupModel = mockk<MarkupModel>()

        mutableHashMap[editor] = mutableSetOf(rangeHighlighter)
        service.setHighlights(mutableHashMap)

        every { editor.markupModel } returns markupModel
        every { markupModel.removeHighlighter(any()) } returns Unit

        service.hideHighlights(editor)
        verify { editor.markupModel }
    }

    @Test
    fun testHideHighlightsEmptyMapForeachNotRun(){
        myFixture.configureByFile("/Methods.java")

        val editor = mockk<Editor>()
        val service = CoverageHighlighterService(project)
        val rangeHighlighter = mockk<RangeHighlighter>()
        val mutableHashMap = HashMap<Editor, MutableSet<RangeHighlighter>>()
        val markupModel = mockk<MarkupModel>()

        mutableHashMap[editor] = mutableSetOf(rangeHighlighter)
        service.setHighlights(mutableHashMap)

        every { editor.markupModel } returns markupModel
        every { markupModel.removeHighlighter(any()) } returns Unit
        // a different editor is passed
        service.hideHighlights(myFixture.editor)
        verify(inverse = true) { editor.markupModel }
    }

    @Test
    fun testShowHighlightsNull(){
        myFixture.configureByFile("/Methods.java")
        val service = CoverageHighlighterService(project)
        val covService = mockk<CoverageDataService>()
        service.setCoverageDataService(covService)
        every { covService.getDiffLines(any()) } returns Unit
        every { covService.classCoveragesMap[any()] } returns null

        service.showHighlights(myFixture.editor, "ClassName")

        TestCase.assertEquals(covService.classCoveragesMap["ClassName"],null)
    }

}