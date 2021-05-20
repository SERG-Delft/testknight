package com.testbuddy.services

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AllClassesSearch
import com.intellij.rt.coverage.data.ProjectData

class CoverageDataService {

    private var previousData: ProjectData? = null
    private var previousSuite: CoverageSuitesBundle? = null
    private var currentData: ProjectData? = null
    private var currentSuite: CoverageSuitesBundle? = null
    // private var classCoveragesMap = mutableMapOf<String, CoverageDiffObject>()

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
    fun getCurrentSuiteData(project: Project): ProjectData? {
        val covDataManager = CoverageDataManager.getInstance(project)
        val suite = covDataManager.currentSuitesBundle
        return suite.coverageData
    }

    /**
     * getDiff for both types of supported views
     */
    fun getDiffLines(project: Project) {
//        previousData.classes["Point"].lines

        if (previousData == null || currentData == null) { return }

        // gets all relevant classes in current project (not imports and org.junit or javax classes)
        val classesInProject = AllClassesSearch.search(GlobalSearchScope.projectScope(project), project)
            .findAll().mapNotNull { it.name }

        print(classesInProject[0])

//        classesInProject.forEach {
//            if(previousData.classes != null) {
//            if(previousData.classes.contains(it))
//        }
    }
}
