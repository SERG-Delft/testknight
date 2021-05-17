package com.testbuddy.com.testbuddy.services

import com.intellij.coverage.CoverageDataManager
import com.intellij.openapi.project.Project
import com.intellij.rt.coverage.data.ProjectData

class CoverageDataService(val project: Project) {

    private val covDataManager = CoverageDataManager.getInstance(project)

    /**
     * Get the current coverage data for the current suite.
     *
     * @return a ProjectData which is null if no coverage suite is selected
     */
    fun getCurrentSuiteData(): ProjectData? {
        val suite = covDataManager.currentSuitesBundle
        return suite.coverageData
    }
}
