package com.testbuddy

import com.intellij.coverage.CoverageDataManager
import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.components.service
import com.testbuddy.com.testbuddy.services.CoverageDataService

class CoverageListener : ExecutionListener {

    override fun processTerminated(
        executorId: String,
        env: ExecutionEnvironment,
        handler: ProcessHandler,
        exitCode: Int
    ) {

        if (executorId == "Coverage") {
            val covDataManager = CoverageDataManager.getInstance(env.project)
            val suite = covDataManager.currentSuitesBundle
            val data = suite.coverageData

            val service = env.project.service<CoverageDataService>()
            service.updateCoverage(suite, data)
        }
    }
}
