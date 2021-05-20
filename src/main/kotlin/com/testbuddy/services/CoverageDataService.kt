package com.testbuddy.com.testbuddy.services

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.openapi.project.Project
import com.intellij.rt.coverage.data.ProjectData

class CoverageDataService(val project: Project) {

    private val covDataManager = CoverageDataManager.getInstance(project)

    private var previousData: ProjectData? = null
    private var previousSuite: CoverageSuitesBundle? = null
    private var currentData: ProjectData? = null
    private var currentSuite: CoverageSuitesBundle? = null

    /**
     * Updates coverage at the end of every execution of runWithCoverage.
     * Swaps current data and suite with previous and sets current to the passed
     * follows after update strategy so caller has to ensure both previous and current are present
     */
    fun updateCoverage(newSuite: CoverageSuitesBundle?, newData: ProjectData?) {
        previousData = currentData
        previousSuite = currentSuite

        currentData = newData
        currentSuite = newSuite
    }
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
