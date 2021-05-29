package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.branchingStatements

import com.intellij.psi.PsiElement
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

data class SwitchStatementChecklistNode(
    override var description: String,
    override val element: PsiElement,
    val switchVariable: String,
    val value: String?,
    val isDefaultCase: Boolean = value == null
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(): PsiElement {
        TODO("Not yet implemented")
    }
}
