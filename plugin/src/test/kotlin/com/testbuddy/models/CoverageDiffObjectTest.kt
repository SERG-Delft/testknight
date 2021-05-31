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

        // allLines not required
        val allLines = emptySet<Int>()
        val coveredPrev = setOf(1, 2, 3)
        val coveredNow = setOf(1, 2, 3, 4, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(obj.linesNewlyRemoved, emptySet<Int>())
    }

    @Test
    fun testEmptyNotCovered() {

        // depends on all 3 attributes
        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(1, 2, 3, 4)
        val coveredNow = setOf(2, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(obj.linesNotCovered, emptySet<Int>())
    }

    @Test
    fun testEmptyNewlyAdded() {

        // allLines not required
        val allLines = emptySet<Int>()
        val coveredPrev = setOf(1, 2, 3, 4, 5)
        val coveredNow = setOf(1, 2, 3, 4, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(obj.linesNewlyAdded, emptySet<Int>())
    }

    @Test
    fun testEmptyCoveredInBoth() {

        // linesCoveredOnBoth isn't dependent on all lines in implementation
        val allLines = emptySet<Int>()
        val coveredPrev = emptySet<Int>()
        val coveredNow = setOf(1, 2, 3, 4, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(
            obj.linesCoveredInBoth,
            emptySet<Int>()
        )
    }

    @Test
    fun testCaseOne() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(1, 2, 3, 4, 5)
        val coveredNow = setOf(1, 2, 3, 4, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(
            obj.linesCoveredInBoth,
            setOf(1, 2, 3, 4, 5)
        )
        TestCase.assertEquals(
            obj.linesNotCovered,
            emptySet<Int>()
        )
        TestCase.assertEquals(
            obj.linesNewlyAdded,
            emptySet<Int>()
        )
        TestCase.assertEquals(
            obj.linesNewlyRemoved,
            emptySet<Int>()
        )
    }

    @Test
    fun testCaseTwo() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = emptySet<Int>()
        val coveredNow = setOf(1, 2, 3, 4, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(
            obj.linesCoveredInBoth,
            emptySet<Int>()
        )
        TestCase.assertEquals(
            obj.linesNotCovered,
            emptySet<Int>()
        )
        TestCase.assertEquals(
            obj.linesNewlyAdded,
            setOf(1, 2, 3, 4, 5)
        )
        TestCase.assertEquals(
            obj.linesNewlyRemoved,
            emptySet<Int>()
        )
    }

    @Test
    fun testCaseThree() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(1, 2, 3, 4, 5)
        val coveredNow = emptySet<Int>()

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(
            obj.linesCoveredInBoth,
            emptySet<Int>()
        )
        TestCase.assertEquals(
            obj.linesNotCovered,
            emptySet<Int>()
        )
        TestCase.assertEquals(
            obj.linesNewlyAdded,
            emptySet<Int>()
        )
        TestCase.assertEquals(
            obj.linesNewlyRemoved,
            setOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun testCaseFour() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(1, 3, 5)
        val coveredNow = setOf(1, 2)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(
            obj.linesCoveredInBoth,
            setOf(1)
        )
        TestCase.assertEquals(
            obj.linesNotCovered,
            setOf(4)
        )
        TestCase.assertEquals(
            obj.linesNewlyAdded,
            setOf(2)
        )
        TestCase.assertEquals(
            obj.linesNewlyRemoved,
            setOf(3, 5)
        )
    }

    @Test
    fun testCaseFive() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(1, 2)
        val coveredNow = setOf(1, 3, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(
            obj.linesCoveredInBoth,
            setOf(1)
        )
        TestCase.assertEquals(
            obj.linesNotCovered,
            setOf(4)
        )
        TestCase.assertEquals(
            obj.linesNewlyAdded,
            setOf(3, 5)
        )
        TestCase.assertEquals(
            obj.linesNewlyRemoved,
            setOf(2)
        )
    }

    @Test
    fun testCaseSix() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(3)
        val coveredNow = setOf(1)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(
            obj.linesCoveredInBoth,
            emptySet<Int>()
        )
        TestCase.assertEquals(
            obj.linesNotCovered,
            setOf(2, 4, 5)
        )
        TestCase.assertEquals(
            obj.linesNewlyAdded,
            setOf(1)
        )
        TestCase.assertEquals(
            obj.linesNewlyRemoved,
            setOf(3)
        )
    }

    @Test
    fun testCaseSeven() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(3, 4, 5)
        val coveredNow = setOf(1, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(
            obj.linesCoveredInBoth,
            setOf(5)
        )
        TestCase.assertEquals(
            obj.linesNotCovered,
            setOf(2)
        )
        TestCase.assertEquals(
            obj.linesNewlyAdded,
            setOf(1)
        )
        TestCase.assertEquals(
            obj.linesNewlyRemoved,
            setOf(3, 4)
        )
    }

    @Test
    fun testCaseEight() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(2, 5)
        val coveredNow = setOf(1, 3, 5)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(
            obj.linesCoveredInBoth,
            setOf(5)
        )
        TestCase.assertEquals(
            obj.linesNotCovered,
            setOf(4)
        )
        TestCase.assertEquals(
            obj.linesNewlyAdded,
            setOf(1, 3)
        )
        TestCase.assertEquals(
            obj.linesNewlyRemoved,
            setOf(2)
        )
    }

    @Test
    fun testCaseNine() {

        val allLines = setOf(1, 2, 3, 4, 5)
        val coveredPrev = setOf(2, 3, 4)
        val coveredNow = setOf(1, 2, 4)

        val obj = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        TestCase.assertEquals(
            obj.linesCoveredInBoth,
            setOf(2, 4)
        )
        TestCase.assertEquals(
            obj.linesNotCovered,
            setOf(5)
        )
        TestCase.assertEquals(
            obj.linesNewlyAdded,
            setOf(1)
        )
        TestCase.assertEquals(
            obj.linesNewlyRemoved,
            setOf(3)
        )
    }
}
