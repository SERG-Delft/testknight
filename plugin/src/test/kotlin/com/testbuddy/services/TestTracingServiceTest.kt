package com.testbuddy.services

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.Test
import java.io.File

internal class TestTracingServiceTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
    }

    override fun getTestDataPath(): String = "testdata"

    @Test
    fun testBasic() {
        val service = TestTracingService(project)

        val file = File("testdata/traceFiles/PointTest,test.tr")

        val coverageData = service.readTraceFile(file)

        assertContainsElements(coverageData.classes["Point"]!!, 8, 9, 10, 11, 14, 30, 31, 32, 50, 54)
    }
}
