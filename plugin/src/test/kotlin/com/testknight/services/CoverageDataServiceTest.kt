package com.testknight.services

import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.rt.coverage.data.ClassData
import com.intellij.rt.coverage.data.LineData
import com.intellij.rt.coverage.data.ProjectData
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testknight.models.CoverageDiffObject
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class CoverageDataServiceTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testReassignmentUpdateCoverage() {
        val service = CoverageDataService(project)
        val currentData = mockk<ProjectData>()
        val currentSuite = mockk<CoverageSuitesBundle>()
        service.currentData = currentData
        service.currentSuite = currentSuite
        val newSuite = mockk<CoverageSuitesBundle>()
        val newData = mockk<ProjectData>()
        every { newSuite.project } returns myFixture.project
        service.updateCoverage(newSuite, newData)
        TestCase.assertEquals(service.currentData, newData)
        TestCase.assertEquals(service.currentSuite, newSuite)
        TestCase.assertEquals(service.previousData, currentData)
        TestCase.assertEquals(service.previousSuite, currentSuite)
    }

    @Test
    fun testBoilerplateSetReturn() {
        val service = CoverageDataService(project)

        val line1 = LineData(
            1,
            "simple description for line 1"
        )
        line1.hits = 2

        val line2 = LineData(
            2,
            "simple description for line 2"
        )
        line2.hits = 4

        val line6 = LineData(6, "simple description for line 6")
        line6.hits = 6

        val classData = mockk<ClassData>()

        every { classData.lines } returns arrayOf(line1, line2)

        every { classData.getLineData(0) } returns line1
        every { classData.getLineData(1) } returns line2

        TestCase.assertEquals(service.getLinesCoveredPreviously(classData), setOf(0, 1))
    }

    @Test
    fun testNullLinesInClassDataPassedPrevious() {
        val service = CoverageDataService(project)

        // some null lines
        val line0 = null
        val line1 = LineData(
            1,
            "simple description for line 1"
        )
        line1.hits = 2

        val line2 = LineData(
            2,
            "simple description for line 2"
        )
        line2.hits = 4

        val line3 = null
        val line4 = null
        val line5 = null

        val line6 = LineData(6, "simple description for line 6")
        line6.hits = 6

        val classData = mockk<ClassData>()

        every { classData.lines } returns arrayOf(line0, line1, line2, line3, line4, line5, line6)

        every { classData.getLineData(0) } returns line0
        every { classData.getLineData(1) } returns line1
        every { classData.getLineData(2) } returns line2
        every { classData.getLineData(3) } returns line3
        every { classData.getLineData(4) } returns line4
        every { classData.getLineData(5) } returns line5
        every { classData.getLineData(6) } returns line6

        TestCase.assertEquals(service.getLinesCoveredPreviously(classData), setOf(1, 2, 6))
    }

    @Test
    fun testFilterByHitsCoveredPreviously() {
        val service = CoverageDataService(project)

        val line0 = LineData(
            0,
            "simple description for line 0"
        )
        line0.hits = 0

        val line1 = LineData(
            1,
            "simple description for line 1"
        )
        line1.hits = 1

        val line2 = LineData(
            2,
            "simple description for line 2"
        )
        line2.hits = -1

        val line3 = LineData(
            3,
            "simple description for line 3"
        )
        line3.hits = Int.MAX_VALUE

        val line4 = LineData(
            4,
            "simple description for line 4"
        )
        line4.hits = Int.MIN_VALUE

        val line5 = LineData(
            5,
            "simple description for line 5"
        )
        line5.hits = 0

        val line6 = LineData(
            6,
            "simple description for line 6"
        )
        line6.hits = 2

        val line7 = null

        val classData = mockk<ClassData>()

        every { classData.lines } returns arrayOf(line0, line1, line2, line3, line4, line5, line6, line7)

        every { classData.getLineData(0) } returns line0
        every { classData.getLineData(1) } returns line1
        every { classData.getLineData(2) } returns line2
        every { classData.getLineData(3) } returns line3
        every { classData.getLineData(4) } returns line4
        every { classData.getLineData(5) } returns line5
        every { classData.getLineData(6) } returns line6
        every { classData.getLineData(7) } returns line7

        TestCase.assertEquals(service.getLinesCoveredPreviously(classData), setOf(1, 3, 6))
    }

    @Test
    fun testBoilerplateSetReturnNewlyCovered() {
        val service = CoverageDataService(project)

        val line1 = LineData(
            1,
            "simple description for line 1"
        )
        line1.hits = 2

        val line2 = LineData(
            2,
            "simple description for line 2"
        )
        line2.hits = 4

        val line3 = LineData(3, "simple description for line 3")
        line3.hits = 6

        val classData = mockk<ClassData>()

        every { classData.lines } returns arrayOf(line1, line2, line3)

        every { classData.getLineData(0) } returns line1
        every { classData.getLineData(1) } returns line2
        every { classData.getLineData(2) } returns line3

        TestCase.assertEquals(
            service.getTotalLinesAndNewlyCoveredLines(classData),
            Pair(setOf(0, 1, 2), setOf(0, 1, 2))
        )
    }

    @Test
    fun testNullLinesInClassDataPassedNew() {
        val service = CoverageDataService(project)

        // some null lines
        val line0 = null
        val line1 = LineData(
            1,
            "simple description for line 1"
        )
        line1.hits = 2

        val line2 = LineData(
            2,
            "simple description for line 2"
        )
        line2.hits = 4

        val line3 = null
        val line4 = null
        val line5 = null

        val line6 = LineData(6, "simple description for line 6")
        line6.hits = 6

        val classData = mockk<ClassData>()

        every { classData.lines } returns arrayOf(line0, line1, line2, line3, line4, line5, line6)

        every { classData.getLineData(0) } returns line0
        every { classData.getLineData(1) } returns line1
        every { classData.getLineData(2) } returns line2
        every { classData.getLineData(3) } returns line3
        every { classData.getLineData(4) } returns line4
        every { classData.getLineData(5) } returns line5
        every { classData.getLineData(6) } returns line6

        TestCase.assertEquals(
            service.getTotalLinesAndNewlyCoveredLines(classData),
            Pair(setOf(1, 2, 6), setOf(1, 2, 6))
        )
    }

    @Test
    fun testFilterByHitsCoveredNewly() {
        val service = CoverageDataService(project)

        // All lines only excludes cases where LineData is null
        // Because LineData can not have negative hits

        val line0 = LineData(
            0,
            "simple description for line 0"
        )
        line0.hits = 0

        val line1 = LineData(
            1,
            "simple description for line 1"
        )
        line1.hits = 1

        val line2 = LineData(
            2,
            "simple description for line 2"
        )
        line2.hits = -1

        val line3 = LineData(
            3,
            "simple description for line 3"
        )
        line3.hits = Int.MAX_VALUE

        val line4 = LineData(
            4,
            "simple description for line 4"
        )
        line4.hits = Int.MIN_VALUE

        val line5 = LineData(
            5,
            "simple description for line 5"
        )
        line5.hits = 0

        val line6 = LineData(
            6,
            "simple description for line 6"
        )
        line6.hits = 2

        val line7 = null

        val classData = mockk<ClassData>()

        every { classData.lines } returns arrayOf(line0, line1, line2, line3, line4, line5, line6, line7)

        every { classData.getLineData(0) } returns line0
        every { classData.getLineData(1) } returns line1
        every { classData.getLineData(2) } returns line2
        every { classData.getLineData(3) } returns line3
        every { classData.getLineData(4) } returns line4
        every { classData.getLineData(5) } returns line5
        every { classData.getLineData(6) } returns line6
        every { classData.getLineData(7) } returns line7

        TestCase.assertEquals(
            service.getTotalLinesAndNewlyCoveredLines(classData),
            Pair(setOf(0, 1, 2, 3, 4, 5, 6), setOf(1, 3, 6))
        )
    }

    @Test
    fun testNullClassDataPreviousCoverage() {
        val service = CoverageDataService(project)
        val classData = null
        TestCase.assertEquals(
            service.getLinesCoveredPreviously(classData),
            emptySet<Int>()
        )
    }

    @Test
    fun testNullClassDataNewCoverage() {
        val service = CoverageDataService(project)
        val classData = null
        TestCase.assertEquals(
            service.getTotalLinesAndNewlyCoveredLines(classData),
            Pair(emptySet<Int>(), emptySet<Int>())
        )
    }

    @Test
    fun testPreviousDataNullGetDiffLines() {
        val service = CoverageDataService(project)
        service.previousData = null
        val obj = CoverageDiffObject(setOf(1, 2, 3), setOf(1, 2, 3), setOf(1, 2, 3))
        val classCoveragesMap = mutableMapOf("PointClass" to obj)
        service.classCoveragesMap = classCoveragesMap
        service.getDiffLines()
        // check that the map hasn't been modified after call
        assertEquals(service.classCoveragesMap, classCoveragesMap)
    }

    @Test
    fun testCurrentDataNullGetDiffLines() {
        val service = CoverageDataService(project)
        service.currentData = null
        val obj = CoverageDiffObject(setOf(1, 2, 3), setOf(1, 2, 3), setOf(1, 2, 3))
        val classCoveragesMap = mutableMapOf("PointClass" to obj)
        service.classCoveragesMap = classCoveragesMap
        service.getDiffLines()
        // check that the map hasn't been modified after call
        assertEquals(service.classCoveragesMap, classCoveragesMap)
    }
}
