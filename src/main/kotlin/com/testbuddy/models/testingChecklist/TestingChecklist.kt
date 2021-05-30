package com.testbuddy.models

import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistClassNode

data class TestingChecklist(val classChecklists: MutableList<TestingChecklistClassNode>)

open class TestingChecklistNode(open var checked: Int = 0)
