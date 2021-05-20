package com.testbuddy.services

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AllClassesSearch
import com.intellij.rt.coverage.data.ClassData
import com.intellij.rt.coverage.data.ProjectData
import com.testbuddy.com.testbuddy.models.CoverageDiffObject

class CoverageDataService {

    private var previousData: ProjectData? = null
    private var previousSuite: CoverageSuitesBundle? = null
    private var currentData: ProjectData? = null
    private var currentSuite: CoverageSuitesBundle? = null
    private var classCoveragesMap = mutableMapOf<String, CoverageDiffObject>()

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
     * Method to modify the classCoveragesMap field.
     * This holds the CoverageDiffObject object for each class in the project relevant to previous and current suite.
     * This object holds data on each line, namely if it is-
     * 1) covered in the suite associated with the latest run
     * 2) covered in the suite associated with the previous run
     */
    fun getDiffLines(project: Project) {
        var allLines = emptySet<Int>()
        var coveredPrev = emptySet<Int>()
        var coveredNow = emptySet<Int>()
        if (previousData == null || currentData == null) {
            return
        }

        // gets all relevant classes in current project (not imports and org.junit or javax classes)
        val classesInProject = AllClassesSearch.search(GlobalSearchScope.projectScope(project), project)
            .findAll().mapNotNull { it.name }

        classesInProject.forEach {
            if (currentData!!.classes.contains(it)) {
                val classData = currentData!!.classes[it]
                allLines = getTotalLinesAndNewlyCoveredLines(classData).first
                coveredNow = getTotalLinesAndNewlyCoveredLines(classData).second
            }

            if (previousData!!.classes.contains(it)) {
                coveredPrev = getLinesCoveredPreviously(previousData!!.classes[it])
            }

            classCoveragesMap[it] = CoverageDiffObject(allLines, coveredPrev, coveredNow)
        }
    }

    /**
     * Helper method to extract set of all lines and lines covered in current suite.
     *
     * @param classData classData object to extract corresponding lineData information.
     * @return a pair of set of all lines and set of lines covered by existing suite.
     */
    private fun getTotalLinesAndNewlyCoveredLines(classData: ClassData?): Pair<Set<Int>, Set<Int>> {
        val allLineSet = mutableSetOf<Int>()
        val coveredNowSet = mutableSetOf<Int>()
        if (classData == null) return Pair(emptySet<Int>(), emptySet<Int>())
        val size = classData.lines.size
        for (i in 0 until size) {
            if (classData.getLineData(i) != null) {
                allLineSet.add(i)
                if (classData.getLineData(i).hits > 0) coveredNowSet.add(i)
            }
        }
        return Pair(allLineSet, coveredNowSet)
    }

    /**
     * Helper method to extract set of all lines and lines covered in current suite.
     *
     * @param classData classData object to extract corresponding lineData information.
     * @return a set of all lines covered by previous suite.
     */
    private fun getLinesCoveredPreviously(classData: ClassData?): Set<Int> {
        val coveredPrevSet = mutableSetOf<Int>()
        if (classData == null) return emptySet()
        val size = classData.lines.size
        for (i in 0 until size) {
            if (classData.getLineData(i) != null) {
                if (classData.getLineData(i).hits > 0) coveredPrevSet.add(i)
            }
        }
        return coveredPrevSet
    }
}
