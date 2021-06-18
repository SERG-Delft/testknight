package com.testknight.models.testingChecklist

import com.testknight.models.testingChecklist.parentNodes.TestingChecklistClassNode

data class TestingChecklist(var classChecklists: MutableList<TestingChecklistClassNode> = mutableListOf())

open class TestingChecklistNode(open var checked: Int = 0)
