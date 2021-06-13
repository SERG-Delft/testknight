package com.testbuddy.services

import com.testbuddy.extensions.TestBuddyTestCase
import org.junit.jupiter.api.Test
import java.io.File

internal class TestTracingServiceTest : TestBuddyTestCase() {

    @Test
    fun testBasic() {
        val service = TestTracingService(project)

        val file = File("testdata/traceFiles/PointTest,test.tr")

        val coverageData = service.readTraceFile(file)

        assertContainsElements(coverageData.classes["Point"]!!, 8, 9, 10, 11, 14, 30, 31, 32, 50, 54)
    }
}
