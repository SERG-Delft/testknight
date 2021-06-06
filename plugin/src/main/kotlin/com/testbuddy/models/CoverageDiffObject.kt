package com.testbuddy.models

import com.intellij.openapi.vfs.VirtualFile

class CoverageDiffObject(
    val allLines: Set<Int>,
    val coveredPrev: Set<Int>,
    val coveredNow: Set<Int>,
    val virtualFile: VirtualFile? = null
) {

    val linesNewlyRemoved: Set<Int>
    val linesNewlyAdded: Set<Int>
    val linesCoveredInBoth: Set<Int>
    val linesNotCovered: Set<Int>

    init {
        linesNewlyRemoved = coveredPrev - coveredNow
        linesNewlyAdded = coveredNow - coveredPrev
        linesCoveredInBoth = coveredPrev.intersect(coveredNow)
        linesNotCovered = (allLines - coveredPrev).intersect(allLines - coveredNow)
    }
}
