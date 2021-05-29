package com.testbuddy.models

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class CoverageDiffObjectTest : BasePlatformTestCase() {

    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

    @Test
    fun testGoodWeatherNewlyRemoved() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(1, 2, 3, 4, 5)
        val coveredNow = setOf(1, 2, 3, 4, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(obj.linesNewlyRemoved, emptySet<Int>())
    }

    @Test
    fun testGoodWeatherNewlyAdded() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(1, 2, 3, 4, 5)
        val coveredNow = setOf(1, 2, 3, 4, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(obj.linesNewlyAdded, emptySet<Int>())
    }

    @Test
    fun testGoodWeatherCoveredInBoth() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(1, 2, 3, 4, 5)
        val coveredNow = setOf(1, 2, 3, 4, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(obj.linesCoveredInBoth,
                setOf(1, 2, 3, 4, 5))
    }

    @Test
    fun testGoodWeatherNotCovered() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(1, 2, 3, 4, 5)
        val coveredNow = setOf(1, 2, 3, 4, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(obj.linesNotCovered, emptySet<Int>())
    }
}
