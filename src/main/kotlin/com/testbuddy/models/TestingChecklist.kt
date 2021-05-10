package com.testbuddy.com.testbuddy.models

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod

data class TestingChecklist(val classChecklists: List<TestingChecklistClassNode>)

abstract class TestingChecklistNode
data class TestingChecklistLeaf(var description: String, val element: PsiElement) : TestingChecklistNode()
data class TestingChecklistClassNode(
    var description: String,
    val children: List<TestingChecklistMethodNode>,
    val element: PsiClass
) : TestingChecklistNode()
data class TestingChecklistMethodNode(
    var description: String,
    val children: List<TestingChecklistLeaf>,
    val element: PsiMethod
) : TestingChecklistNode()
