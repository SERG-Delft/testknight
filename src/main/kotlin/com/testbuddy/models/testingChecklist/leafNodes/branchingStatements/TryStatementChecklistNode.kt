package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.branchingStatements

import com.intellij.psi.PsiElement
import com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

data class TryStatementChecklistNode(
    override var description: String,
    override val element: PsiElement,
    val exceptionName: String?,
    val isExceptionThrown: Boolean = exceptionName != null
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(): PsiElement {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TryStatementChecklistNode

        if (description != other.description) return false
        if (element != other.element) return false
        if (exceptionName != other.exceptionName) return false
        if (isExceptionThrown != other.isExceptionThrown) return false

        return true
    }

    override fun hashCode(): Int {
        var result = description.hashCode()
        result = 31 * result + element.hashCode()
        result = 31 * result + (exceptionName?.hashCode() ?: 0)
        result = 31 * result + isExceptionThrown.hashCode()
        return result
    }


}
