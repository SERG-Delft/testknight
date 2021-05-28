package com.testbuddy.services

import com.intellij.rt.coverage.data.ClassData
import com.intellij.rt.coverage.data.LineData
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class CoverageDataServiceTest : BasePlatformTestCase(){

    val service = CoverageDataService()

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testBoilerplateSetReturn() {

        val line1 = LineData(1,
                "simple description for line 1")
        line1.hits = 2

        val line2 = LineData(2,
                "simple description for line 2")
        line2.hits = 4

        val line6 = LineData(6, "simple description for line 3")
        line6.hits = 6

        val classData = mockk<ClassData>()

        every { classData.lines } returns arrayOf(line1, line2)

        every { classData.getLineData(0) } returns line1
        every { classData.getLineData(1) } returns line2

        TestCase.assertEquals(service.getLinesCoveredPreviously(classData), setOf(0, 1))
    }

    @Test
    fun testNullLinesInClassDataPassed(){

        val line0 = null
        val line1 = LineData(1,
                "simple description for line 1")
        line1.hits = 2

        val line2 = LineData(2,
                "simple description for line 2")
        line2.hits = 4

        val line3 = null
        val line4 = null
        val line5 = null

        val line6 = LineData(6,"simple description for line 3")
        line6.hits = 6

        val classData = mockk<ClassData>()

        every { classData.lines } returns arrayOf(line0,line1,line2,line3,line4,line5,line6)

        every { classData.getLineData(0)} returns line0
        every { classData.getLineData(1)} returns line1
        every { classData.getLineData(2)} returns line2
        every { classData.getLineData(3)} returns line3
        every { classData.getLineData(4)} returns line4
        every { classData.getLineData(5)} returns line5
        every { classData.getLineData(6)} returns line6


        TestCase.assertEquals(service.getLinesCoveredPreviously(classData),setOf(1,2,6))
    }

    @Test
    fun testMethod(){
        TestCase.assertTrue(true)
    }

}