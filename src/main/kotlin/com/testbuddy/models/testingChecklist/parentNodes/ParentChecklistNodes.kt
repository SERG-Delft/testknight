package com.testbuddy.models

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.models.TestingChecklistNode

abstract class TestingChecklistParentNode : TestingChecklistNode()
data class TestingChecklistClassNode(
    var description: String,
    val children: MutableList<TestingChecklistMethodNode>,
    val element: PsiClass,
    override var checked: Int = 0
    // val parent: TestingChecklist
) : TestingChecklistParentNode()

data class TestingChecklistMethodNode(
    var description: String,
    val children: MutableList<TestingChecklistLeafNode>,
    val element: PsiMethod,
    override var checked: Int = 0
    // val parent: TestingChecklistClassNode?
) : TestingChecklistParentNode()
