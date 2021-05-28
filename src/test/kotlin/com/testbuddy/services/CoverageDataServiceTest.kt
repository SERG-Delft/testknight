package com.testbuddy.services

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.psi.stubs.prebuiltStubsProvider
import com.intellij.rt.coverage.data.ClassData
import com.intellij.rt.coverage.data.LineData
import com.intellij.rt.coverage.data.ProjectData
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.spyk
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
     fun testBoilerplateSetReturn(){

        val line1 = LineData(1,
                "simple description for line 1")
        line1.hits = 2

        val line2 = LineData(2,
                "simple description for line 2")
        line2.hits = 4

        val line6 = LineData(6,"simple description for line 3")
        line6.hits = 6

        val classDatas = mockk<ClassData>()

        every { classDatas.lines } returns arrayOf(line1,line2)

        every { classDatas.getLineData(0)} returns line1
        every { classDatas.getLineData(1)} returns line2

        TestCase.assertEquals(service.getLinesCoveredPreviously(classDatas),setOf(0,1))
    }

    @Test
    fun testMethod(){
        TestCase.assertTrue(true)
    }

}