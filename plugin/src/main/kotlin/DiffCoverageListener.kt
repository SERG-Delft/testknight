package com.testbuddy

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageSuiteListener
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.testbuddy.com.testbuddy.services.CoverageDataService

class DiffCoverageListener(val project: Project) : CoverageSuiteListener {

    private val covDataService = project.service<CoverageDataService>()
    private val covDataManager = CoverageDataManager.getInstance(project)

    override fun beforeSuiteChosen() {
        // pass
    }

    override fun afterSuiteChosen() {

        val suite = covDataManager.currentSuitesBundle
        val data = suite.coverageData
        val service = project.service<CoverageDataService>()
        service.setIsDiffAvailable(true)

        covDataService.updateCoverage(suite, data)
    }
}
