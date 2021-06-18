package com.testknight.listeners

import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.testknight.services.UsageDataService

class TestKnightExecutionListener : ExecutionListener {

    override fun processStarted(executorId: String, env: ExecutionEnvironment, handler: ProcessHandler) {
        if (executorId == "Coverage") UsageDataService.instance.recordRunWithCoverage()
    }
}
