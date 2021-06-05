package com.testbuddy.models

data class CoverageStatsObject(
    val coveredLines: Int,
    val allLines: Int,
    val percentageCovered: Int,
    val percentChange: Int
)
