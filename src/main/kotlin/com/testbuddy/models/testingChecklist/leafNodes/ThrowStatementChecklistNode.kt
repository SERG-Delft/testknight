package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiThrowStatement

class ThrowStatementChecklistNode(
    override var description: String,
    override val element: PsiThrowStatement
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(): PsiElement {
        TODO("Not yet implemented")
    }
}