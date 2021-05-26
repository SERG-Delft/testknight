package com.testbuddy.services

import com.intellij.coverage.CoverageDataManager
import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtilRt
import com.testbuddy.com.testbuddy.models.TestCoverageData
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class TestTracingService(val project: Project) {

    private val coverageDataManager = CoverageDataManager.getInstance(project)

    /**
     * Get the lines of code tested by a given test.
     *
     * @param test the test in string format: ClassName,testName.
     * @return a TestCoverageObject representing the lines covered by the test.
     */
    fun getLinesForTest(test: String): TestCoverageData {

        val currentSuitesBundle = coverageDataManager.currentSuitesBundle
            ?: throw FileNotFoundException("no coverage data")

        val traceFile: File = getTraceFile(test, currentSuitesBundle)
            ?: throw FileNotFoundException("traces not found")

        return readTraceFile(traceFile)
    }

    /**
     * Gets the trace file given a coverage suite and testName.
     *
     * @param test the test.
     * @param coverageSuitesBundle the coverage suite to extract the data from.
     * @return the file pointer of the trace file.
     */
    private fun getTraceFile(test: String, coverageSuitesBundle: CoverageSuitesBundle): File? {

        val traceDirs = coverageSuitesBundle.suites.map {
            val filePath = it.coverageDataFileName
            val dirName = FileUtilRt.getNameWithoutExtension(File(filePath).name)
            val parentDir = File(filePath).parentFile
            File(parentDir, dirName)
        }

        traceDirs
            .sortedByDescending { it.lastModified() }
            .forEach { dir ->

                dir.listFiles()?.forEach { file ->
                    if (file.name == "$test.tr") {
                        return file
                    }
                }
            }

        return null
    }

    /**
     * Read a trace file and extract the information into a TestCoverageData object.
     *
     * @param traceFile the traceFile.
     * @return the lines of code covered by the test.
     */
    @Suppress("TooGenericExceptionCaught")
    fun readTraceFile(traceFile: File): TestCoverageData {

        try {
            val coverage = TestCoverageData(traceFile.nameWithoutExtension)

            val stream = DataInputStream(FileInputStream(traceFile))

            val numClasses = stream.readInt()

            repeat(numClasses) {

                val className = stream.readUTF()
                val linesSize = stream.readInt()

                coverage.classes[className] = mutableListOf()

                repeat(linesSize) {
                    val line = stream.readInt()
                    (coverage.classes[className] as MutableList).add(line)
                }
            }
            stream.close()
            return coverage
        } catch (ex: IOException) {

            println("tracefile could not be read" + ex.message)
            throw ex
        }
    }
}
