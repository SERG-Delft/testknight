package com.testbuddy

import com.intellij.coverage.CoverageDataManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener

class InitializationListener : ProjectManagerListener {

    override fun projectOpened(project: Project) {
        val covDataManager = CoverageDataManager.getInstance(project)
        covDataManager.addSuiteListener(DiffCoverageListener(), null)
    }
}
