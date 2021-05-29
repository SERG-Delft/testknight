package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.branchingStatements

import com.intellij.psi.PsiElement
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

class SwitchStatementChecklistNode(
    override var description: String,
    override val element: PsiElement,
    val switchVariable: String,
    val value: String,
    val defaultCase: Boolean
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(): PsiElement {
        TODO("Not yet implemented")
    }

}
