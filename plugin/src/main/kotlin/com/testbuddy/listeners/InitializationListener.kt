package com.testbuddy.listeners

import com.intellij.coverage.CoverageDataManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.testbuddy.extensions.DiffCoverageListener
import com.testbuddy.services.CoverageDataService

class InitializationListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        val covDataManager = CoverageDataManager.getInstance(project)
        val covDataService = project.service<CoverageDataService>()
        covDataManager.addSuiteListener(DiffCoverageListener(project), covDataService)
    }
}
