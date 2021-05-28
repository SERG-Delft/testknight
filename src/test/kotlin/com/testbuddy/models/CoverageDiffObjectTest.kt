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

    // temp test
    @Test
    fun testSilly() {
        TestCase.assertTrue(true)
    }
}
