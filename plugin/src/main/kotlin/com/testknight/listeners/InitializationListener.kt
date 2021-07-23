package com.testknight.listeners

import com.intellij.coverage.CoverageDataManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.testknight.extensions.DiffCoverageListener
import com.testknight.services.checklist.ChecklistTreeService
import com.testknight.services.CoverageDataService

class InitializationListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        val covDataManager = CoverageDataManager.getInstance(project)
        val covDataService = project.service<CoverageDataService>()
        project.service<ChecklistTreeService>().initUiTree()
        covDataManager.addSuiteListener(DiffCoverageListener(project), covDataService)
    }
}
