package com.testbuddy.listeners

import com.intellij.execution.testframework.AbstractTestProxy
import com.intellij.execution.testframework.TestStatusListener

class TestResultLoggingListener : TestStatusListener() {

    override fun testSuiteFinished(root: AbstractTestProxy?) {
        println("AAAAAAAAAAAAAAAAAAAAAAA")
    }
}
