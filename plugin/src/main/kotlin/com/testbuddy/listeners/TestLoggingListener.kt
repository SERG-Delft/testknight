package com.testbuddy.listeners

import com.intellij.execution.testframework.AbstractTestProxy
import com.intellij.execution.testframework.TestStatusListener
import com.testbuddy.services.UsageDataService

class TestLoggingListener : TestStatusListener() {

    override fun testSuiteFinished(root: AbstractTestProxy?) {

        root ?: return

        for (test in root.allTests) {

            if (test.isLeaf) {
                if (!test.isPassed) UsageDataService.instance.logTestFail()
                UsageDataService.instance.logTestRun()
            }
        }
    }
}
