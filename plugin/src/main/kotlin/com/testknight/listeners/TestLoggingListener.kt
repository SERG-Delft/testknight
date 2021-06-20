package com.testknight.listeners

import com.intellij.execution.testframework.AbstractTestProxy
import com.intellij.execution.testframework.TestStatusListener
import com.testknight.services.UsageDataService

class TestLoggingListener : TestStatusListener() {

    override fun testSuiteFinished(root: AbstractTestProxy?) {
        root ?: return
        UsageDataService.instance.logTests(root)
    }
}
