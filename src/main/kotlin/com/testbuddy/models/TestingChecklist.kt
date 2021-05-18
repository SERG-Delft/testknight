package com.testbuddy.models

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod

data class TestingChecklist(val classChecklists: List<TestingChecklistClassNode>)

abstract class TestingChecklistNode
data class TestingChecklistLeafNode(var description: String, val element: PsiElement) : TestingChecklistNode()
abstract class TestingChecklistParentNode : TestingChecklistNode()
data class TestingChecklistClassNode(
    var description: String,
    val children: List<TestingChecklistMethodNode>,
    val element: PsiClass
//    val parent: TestingChecklist
) : TestingChecklistParentNode()
data class TestingChecklistMethodNode(
    var description: String,
    val children: List<TestingChecklistLeafNode>,
    val element: PsiMethod
//    val parent: TestingChecklistClassNode?
) : TestingChecklistParentNode()
