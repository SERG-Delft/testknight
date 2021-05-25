package com.testbuddy.com.testbuddy.services

import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtilRt
import com.testbuddy.com.testbuddy.models.TestCoverageData
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream

class TestTracingService(val project: Project) {

    private fun getTraceFile(test: String, coverageSuitesBundle: CoverageSuitesBundle): File? {
        val traceDirs = coverageSuitesBundle.suites.map {
            val filePath = it.coverageDataFileName
            val dirName = FileUtilRt.getNameWithoutExtension(File(filePath).name)
            val parentDir = File(filePath).parentFile
            File(parentDir, dirName)
        }

        traceDirs.forEach { dir ->

            dir.listFiles()?.forEach { file ->
                if (file.name == "$test.tr") {
                    return file
                }
            }
        }

        return null
    }

    private fun readTraceFile(traceFile: File): TestCoverageData {

        val coverage = TestCoverageData(traceFile.nameWithoutExtension)

        val stream = DataInputStream(FileInputStream(traceFile))

        val numClasses = stream.readInt()

        for (c in 0 until numClasses) {

            val className = stream.readUTF()
            val linesSize = stream.readInt()

            coverage.classes[className] = mutableListOf()

            for (l in 0 until linesSize) {
                val line = stream.readInt()
                (coverage.classes[className] as MutableList).add(line)
            }
        }

        stream.close()
        return coverage
    }

}
