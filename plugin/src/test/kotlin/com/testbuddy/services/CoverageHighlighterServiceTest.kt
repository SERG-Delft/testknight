package com.testbuddy.services

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.TextAttributesKey
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
    fun testRefreshHighlightsHideHighlightsCalled(){
        val service = CoverageHighlighterService(project)
        val editor = mockk<Editor>()

        service.refreshHighlights(editor, "ClassName")

        verify {service.hideHighlights(editor)}

    }

    @Test
    fun testRefreshHighlightsShowHighlightsCalled(){
        val service = CoverageHighlighterService(project)
        val editor = mockk<Editor>()
        val a = mockk<CoverageHighlighterService>(relaxed = true)
        //val a = spyk<CoverageHighlighterService>

        every { a.refreshHighlights(any(), any())} returns Unit
        a.refreshHighlights(editor,"ClassName")
        verify { a.refreshHighlights(editor, "ClassName") }

        //verify {service.showHighlights(editor, "ClassName")}
        //service.refreshHighlights(editor, "ClassName")



    }

}