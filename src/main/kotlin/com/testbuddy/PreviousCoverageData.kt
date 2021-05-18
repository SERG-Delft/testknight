package com.testbuddy.com.testbuddy

import com.intellij.coverage.CoverageSuitesBundle
import com.intellij.rt.coverage.data.ProjectData

/**
 * Stores the previous coverage data
 */
object PreviousCoverageData {
    var previousData: ProjectData? = null
    var previousSuite: CoverageSuitesBundle? = null
}
