package com.testbuddy.models

import com.intellij.openapi.vfs.VirtualFile

class CoverageDiffObject(
    val allLines: Set<Int> = emptySet(),
    val coveredPrev: Set<Int> = emptySet(),
    val coveredNow: Set<Int> = emptySet(),
    var prevStamp: Long = 0,
    var currStamp: Long = 0,
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
