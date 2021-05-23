package com.testbuddy

import com.intellij.coverage.CoverageDataManager
import com.intellij.execution.ExecutionListener
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.components.service
import com.testbuddy.services.CoverageDataService

class CoverageListener : ExecutionListener {

    @Suppress("MagicNumber")
    override fun processTerminated(
        executorId: String,
        env: ExecutionEnvironment,
        handler: ProcessHandler,
        exitCode: Int
    ) {
        handler.waitFor(1000)

        if (executorId == "Coverage") {
            val covDataManager = CoverageDataManager.getInstance(env.project)
            val suite = covDataManager.currentSuitesBundle
            val data = suite.coverageData

            val service = env.project.service<CoverageDataService>()
            service.updateCoverage(suite, data)
        }
    }
}
