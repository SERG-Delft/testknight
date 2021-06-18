package com.testknight.models

class TestCoverageData(val testName: String) {
    val classes: MutableMap<String, MutableList<Int>> = mutableMapOf()
}
