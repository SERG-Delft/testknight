package com.testknight.extensions

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageSuiteListener
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.testknight.services.CoverageDataService
import com.testknight.services.CoverageHighlighterService
import com.testknight.settings.SettingsService

class DiffCoverageListener(val project: Project) : CoverageSuiteListener {

    private val covDataService = project.service<CoverageDataService>()
    private val covDataManager = CoverageDataManager.getInstance(project)

    override fun beforeSuiteChosen() {
        // pass
    }

    override fun afterSuiteChosen() {

        val suite = covDataManager.currentSuitesBundle
        val data = suite.coverageData

        covDataService.updateCoverage(suite, data)

        if (SettingsService.state.coverageSettings.showIntegratedView) {

            project.service<CoverageHighlighterService>().rebuildHighlights()
        }
    }
}
