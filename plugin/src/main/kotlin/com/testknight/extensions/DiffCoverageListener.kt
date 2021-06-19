package com.testknight.extensions

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageSuiteListener
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
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
        DataManager.getInstance().dataContextFromFocusAsync
            .onSuccess {
                val actionEvent = AnActionEvent.createFromDataContext("DiffCoverageListener", null, it)
                val action = ActionManager.getInstance().getAction("LoadCoverageAction")

                if (actionEvent.project != null) {
                    action.actionPerformed(actionEvent)
                }
            }

        if (SettingsService.state.coverageSettings.showIntegratedView) {

            project.service<CoverageHighlighterService>().rebuildHighlights()
        }
    }
}
