package com.testbuddy.models.testingChecklist

import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistClassNode
// import com.testbuddy.utilities.LocalDateTimeConverter

data class TestingChecklist(var classChecklists: MutableList<TestingChecklistClassNode> = mutableListOf())

open class TestingChecklistNode(open var checked: Int = 0)
