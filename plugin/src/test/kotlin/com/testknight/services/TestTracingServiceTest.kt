package com.testknight.services

import com.intellij.testFramework.UsefulTestCase
import com.intellij.util.ThrowableRunnable
import com.testknight.exceptions.CorruptedTraceFileException
import com.testknight.extensions.TestKnightTestCase
import com.testknight.models.TestCoverageData
import org.junit.jupiter.api.Test
import java.io.File

internal class TestTracingServiceTest : TestKnightTestCase() {

    @Test
    fun testTraceFileReading() {
        val service = TestTracingService(project)

        val file = File("testdata/traceFiles/PointTest,test.tr")

        val coverageData = service.readTraceFile(file)

        assertContainsElements(coverageData.classes["Point"]!!, 8, 9, 10, 11, 14, 30, 31, 32, 50, 54)
    }

    @Test
    fun testInvalidTraceFileReading() {
        val service = TestTracingService(project)

        val file = File("testdata/traceFiles/BadPointTest,test.tr")

        assertThrows(
            CorruptedTraceFileException::class.java,
            ThrowableRunnable {
                service.readTraceFile(file)
            }
        )
    }

    fun testHighlightEditor() {
        val service = TestTracingService(project)

        val covData = TestCoverageData("test")
        covData.classes["test.Person"] = mutableListOf(1, 3, 3)
        service.activeCovData = covData

        myFixture.configureByFile("Person.java")
        val editor = myFixture.editor

        service.highlightEditor(editor)

        UsefulTestCase.assertSize(3, editor.markupModel.allHighlighters)
    }
}
