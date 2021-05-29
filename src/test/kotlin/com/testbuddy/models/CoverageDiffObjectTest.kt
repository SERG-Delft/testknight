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
    fun testEmptyNewlyRemoved() {

        //allLines not required
        val allLines = emptySet<Int>()
        val coveredPrev = setOf(1, 2, 3)
        val coveredNow = setOf(1, 2, 3, 4, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(obj.linesNewlyRemoved, emptySet<Int>())
    }

    @Test
    fun testEmptyNotCovered() {

        //depends on all 3 attributes
        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(1, 2, 3, 4)
        val coveredNow = setOf(2, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(obj.linesNotCovered, emptySet<Int>())
    }

    @Test
    fun testEmptyNewlyAdded() {

        //allLines not required
        val allLines = emptySet<Int>()
        val coveredPrev = setOf(1, 2, 3, 4, 5)
        val coveredNow = setOf(1, 2, 3, 4, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(obj.linesNewlyAdded, emptySet<Int>())
    }

    @Test
    fun testEmptyCoveredInBoth() {

        //linesCoveredOnBoth isn't dependent on all lines in implementation
        val allLines = emptySet<Int>()
        val coveredPrev = emptySet<Int>()
        val coveredNow = setOf(1, 2, 3, 4, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(obj.linesCoveredInBoth,
                emptySet<Int>())

    }
}
