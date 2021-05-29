package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes

import com.intellij.psi.PsiElement
import com.testbuddy.models.TestingChecklistNode

abstract class TestingChecklistLeafNode(
    open var description: String,
    open val element: PsiElement,
    override var checked: Int = 0
    // val parent: TestingChecklistMethodNode
) : TestingChecklistNode() {
    abstract fun generateTestMethod(): PsiElement
}
