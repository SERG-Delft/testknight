package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiParameter

data class ParameterChecklistNode(
    override var description: String,
    override val element: PsiParameter,
    val parameterName: String,
    val suggestedValue: String
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(): PsiElement {
        TODO("Not yet implemented")
    }
}
