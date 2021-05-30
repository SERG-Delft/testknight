package com.testbuddy.com.testbuddy.models

class TestCoverageData(val testName: String) {
    val classes: MutableMap<String, MutableList<Int>> = mutableMapOf()
}
