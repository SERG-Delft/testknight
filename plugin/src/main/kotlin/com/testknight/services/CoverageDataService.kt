package com.testknight.services

import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AllClassesSearch
import com.intellij.rt.coverage.data.ClassData
import com.intellij.rt.coverage.data.ProjectData
import com.testknight.exceptions.ProjectNotFoundException
import com.testknight.models.CoverageDiffObject

class CoverageDataService(val project: Project) : Disposable {

    var previousData: ProjectData? = null
    var previousSuite: CoverageSuitesBundle? = null
    var currentData: ProjectData? = null
    var currentSuite: CoverageSuitesBundle? = null
    var classCoveragesMap = mutableMapOf<String, CoverageDiffObject>()

    fun isDiffAvailable(className: String, fileStamp: Long): Boolean {
        val covObj = classCoveragesMap[className] ?: return false
        return (fileStamp == covObj.currStamp && covObj.prevStamp == covObj.currStamp)
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

        val testAnalyzerService = TestAnalyzerService()

        val project = newSuite?.project ?: throw ProjectNotFoundException()

        // Temporary class to filter out deleted classes.
        val tempClassMap = mutableMapOf<String, CoverageDiffObject>()
        AllClassesSearch.search(GlobalSearchScope.projectScope(project), project)
            .findAll()
            .forEach {

                if (it == null || testAnalyzerService.isTestClass(it) || it.qualifiedName == null) {
                    return@forEach
                }

                // Create copy from old map, and remove non existing classes.
                if (classCoveragesMap.keys.contains(it.qualifiedName)) {
                    tempClassMap[it.qualifiedName!!] = classCoveragesMap[it.qualifiedName!!]!!
                } else {
                    tempClassMap[it.qualifiedName!!] = CoverageDiffObject()
                }

                val vFile = it.containingFile.virtualFile
                tempClassMap[it.qualifiedName!!]!!.prevStamp = tempClassMap[it.qualifiedName!!]!!.currStamp
                tempClassMap[it.qualifiedName!!]!!.currStamp = vFile.modificationStamp
            }

        classCoveragesMap = tempClassMap
    }

    /**
     * Method to modify the classCoveragesMap field.
     * This holds the CoverageDiffObject object for each class in the project relevant to previous and current suite.
     * This object holds data on each line, namely if it is-
     * 1) covered in the suite associated with the latest run
     * 2) covered in the suite associated with the previous run
     */
    fun getDiffLines() {

        if (currentData == null) return

        val testAnalyzerService = project.service<TestAnalyzerService>()

        // gets all relevant classes in current project (not imports and org.junit or javax classes)
        // filters out all test classes because we aren't interested in tests for those
        AllClassesSearch.search(GlobalSearchScope.projectScope(project), project)
            .findAll()
            .forEach {

                if (it == null || testAnalyzerService.isTestClass(it) || it.qualifiedName == null) {
                    return@forEach
                }

                var allLinesNow = emptySet<Int>()
                var allLinesPrev = emptySet<Int>()
                var coveredPrev = emptySet<Int>()
                var coveredNow = emptySet<Int>()

                if (currentData!!.classes.contains(it.qualifiedName)) {
                    val classData = currentData!!.classes[it.qualifiedName]
                    allLinesNow = getTotalLinesAndNewlyCoveredLines(classData).first
                    coveredNow = getTotalLinesAndNewlyCoveredLines(classData).second
                }

                if (previousData != null && previousData!!.classes.contains(it.qualifiedName)) {
                    allLinesPrev = getTotalLinesAndNewlyCoveredLines(previousData!!.classes[it.qualifiedName]).first
                    coveredPrev = getTotalLinesAndNewlyCoveredLines(previousData!!.classes[it.qualifiedName]).second
                }

                val vFile = it.containingFile.virtualFile
                val oldObj = classCoveragesMap[it.qualifiedName!!]
                classCoveragesMap[it.qualifiedName!!] = CoverageDiffObject(
                    allLinesPrev,
                    allLinesNow,
                    coveredPrev,
                    coveredNow,
                    oldObj?.prevStamp ?: 0,
                    oldObj?.currStamp ?: 0,
                    vFile
                )
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
