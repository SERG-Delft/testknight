package com.testbuddy

import com.intellij.coverage.CoverageDataManager
import com.intellij.execution.ExecutionListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.testbuddy.com.testbuddy.PreviousCoverageData

class CoverageListener : ExecutionListener {

    override fun processStarting(executorId: String, env: ExecutionEnvironment) {

        val covDataManager = CoverageDataManager.getInstance(env.project)
        val suite = covDataManager.currentSuitesBundle

        // if we are running with coverage update the previous coverage data
        if (executorId == "Coverage") {
            PreviousCoverageData.previousSuite = suite
            if (suite != null) PreviousCoverageData.previousData = covDataManager.currentSuitesBundle.coverageData
        }
    }
}
