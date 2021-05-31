package com.testbuddy.com.testbuddy.services

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AllClassesSearch
import com.intellij.rt.coverage.data.ClassData
import com.intellij.rt.coverage.data.ProjectData
import com.testbuddy.com.testbuddy.models.CoverageDiffObject

class CoverageDataService : Disposable {

    var previousData: ProjectData? = null
    var previousSuite: CoverageSuitesBundle? = null
    var currentData: ProjectData? = null
    var currentSuite: CoverageSuitesBundle? = null
    var classCoveragesMap = mutableMapOf<String, CoverageDiffObject>()
    private var isDiffAvailable = true

    /**
     * Getter to get isDiffAvailable.
     *
     * @return isDiffAvailable.
     */
    fun getIsDiffAvailable(): Boolean {
        return isDiffAvailable
    }

    /**
     * Getter to get isDiffAvailable.
     *
     * @return isDiffAvailable.
     */
    fun setIsDiffAvailable(flag: Boolean): Boolean {
        isDiffAvailable = flag
        return isDiffAvailable
    }

    /**
     * Sets previous data and suite to null.
     * This is required to not show wrong diff info if source code is modified after run
     * In this case the current suite at this point would have outdated
     */
    fun resetCurrentDataAndMap() {
        currentData = null
        currentSuite = null
        // code to be refactored after updating listener
        // classCoveragesMap = mutableMapOf<String, CoverageDiffObject>()
    }

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
        if (currentData == null) return
        if (previousData == null) {
            isDiffAvailable = false
            return
        }

        val testAnalyzerService = TestAnalyzerService()

        // gets all relevant classes in current project (not imports and org.junit or javax classes)
        // filters out all test classes because we aren't interested in tests for those
        val classesInProject = AllClassesSearch.search(GlobalSearchScope.projectScope(project), project)
            .findAll()
            .filter { !testAnalyzerService.isTestClass(it) }
            .mapNotNull { it.name }

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
    fun getTotalLinesAndNewlyCoveredLines(classData: ClassData?): Pair<Set<Int>, Set<Int>> {
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
    fun getLinesCoveredPreviously(classData: ClassData?): Set<Int> {
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

    override fun dispose() {
        // just use default garbage collection
    }
}
