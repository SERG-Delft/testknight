package com.testbuddy.models.testingChecklist.parentNodes

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.testbuddy.models.testingChecklist.TestingChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

abstract class TestingChecklistParentNode : TestingChecklistNode()

data class TestingChecklistClassNode(
    var description: String,
    val children: MutableList<TestingChecklistMethodNode>,
    val element: PsiClass,
) : TestingChecklistParentNode()

data class TestingChecklistMethodNode(
    var description: String,
    val children: MutableList<TestingChecklistLeafNode>,
    val element: PsiMethod,
) : TestingChecklistParentNode()
