package com.testbuddy.com.testbuddy.services

import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtilRt
import java.io.File

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
}
