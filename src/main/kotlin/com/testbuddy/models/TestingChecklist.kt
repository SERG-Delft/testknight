package com.testbuddy.models

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod

data class TestingChecklist(val classChecklists: MutableList<TestingChecklistClassNode>)

open class TestingChecklistNode(open var checked: Int = 0)
data class TestingChecklistLeafNode(
    var description: String,
    val element: PsiElement,
    override var checked: Int = 0
    // val parent: TestingChecklistMethodNode
) : TestingChecklistNode()
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
