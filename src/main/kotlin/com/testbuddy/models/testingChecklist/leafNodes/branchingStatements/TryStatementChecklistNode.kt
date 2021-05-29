package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.branchingStatements

import com.intellij.psi.PsiElement
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

data class TryStatementChecklistNode(
    override var description: String,
    override val element: PsiElement,
    val exceptionName: String,
    val isExceptionThrown: Boolean
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(): PsiElement {
        TODO("Not yet implemented")
    }

}
