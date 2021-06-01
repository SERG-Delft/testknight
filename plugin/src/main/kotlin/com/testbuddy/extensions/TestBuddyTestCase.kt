package com.testbuddy.extensions

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.testbuddy.settings.SettingsService

open class TestBuddyTestCase : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        SettingsService.instance.resetState()
    }

    override fun getTestDataPath() = "testdata"
}
