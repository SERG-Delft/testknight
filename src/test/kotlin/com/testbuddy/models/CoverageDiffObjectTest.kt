package com.testbuddy.models

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Before

class CoverageDiffObjectTest: BasePlatformTestCase() {


    @Before
    public override fun setUp() {
        super.setUp()
    }

    public override fun getTestDataPath(): String {
        return "testdata"
    }

}