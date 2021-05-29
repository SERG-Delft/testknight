package com.testbuddy.models

class CoverageDiffObject(val allLines: Set<Int>, val coveredPrev: Set<Int>, val coveredNow: Set<Int>) {

    val linesNewlyRemoved: Set<Int>
    val linesNewlyAdded: Set<Int>
    val linesCoveredInBoth: Set<Int>
    val linesNotCovered: Set<Int>

    init {
        linesNewlyRemoved = coveredPrev - coveredNow
        linesNewlyAdded = coveredNow - coveredPrev
        linesCoveredInBoth = coveredPrev.intersect(coveredNow)
        linesNotCovered = allLines - linesCoveredInBoth
    }
}
