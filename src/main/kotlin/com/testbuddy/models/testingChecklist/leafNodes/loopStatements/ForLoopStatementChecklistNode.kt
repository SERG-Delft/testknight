package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.loopStatements

import com.intellij.psi.PsiElement
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

class ForLoopStatementChecklistNode(
    override var description: String,
    override val element: PsiElement
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(): PsiElement {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ForLoopStatementChecklistNode

        if (description != other.description) return false
        if (element != other.element) return false

        return true
    }

    override fun hashCode(): Int {
        var result = description.hashCode()
        result = 31 * result + element.hashCode()
        return result
    }


}