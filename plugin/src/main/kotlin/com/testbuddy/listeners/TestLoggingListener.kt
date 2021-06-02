package com.testbuddy.listeners

import com.intellij.execution.testframework.AbstractTestProxy
import com.intellij.execution.testframework.TestStatusListener
import com.testbuddy.services.UsageDataService

class TestLoggingListener : TestStatusListener() {

    override fun testSuiteFinished(root: AbstractTestProxy?) {
        root ?: return
        UsageDataService.instance.logTests(root)
    }
}
