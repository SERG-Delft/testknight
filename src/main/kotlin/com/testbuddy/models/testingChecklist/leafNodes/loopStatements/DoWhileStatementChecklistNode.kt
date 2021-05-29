package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.loopStatements

import com.intellij.psi.PsiElement
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

class DoWhileStatementChecklistNode(
    override var description: String,
    override val element: PsiElement
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(): PsiElement {
        TODO("Not yet implemented")
    }
}