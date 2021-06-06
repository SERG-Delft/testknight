package com.testbuddy.listeners

import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.testbuddy.services.UsageDataService

class TestBuddyExecutionListener : ExecutionListener {

    override fun processStarted(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
        if (executorId == "Coverage") UsageDataService.instance.recordRunWithCoverage()
    }
}
