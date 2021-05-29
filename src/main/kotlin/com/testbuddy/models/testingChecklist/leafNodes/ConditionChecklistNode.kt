package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes

import com.intellij.psi.PsiElement

class ConditionChecklistNode(
    override var description: String,
    override val element: PsiElement
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(): PsiElement {
        TODO("Not yet implemented")
    }
}