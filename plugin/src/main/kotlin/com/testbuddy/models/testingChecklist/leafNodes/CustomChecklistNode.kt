package com.testbuddy.com.testbuddy.models.testingChecklist.leafNodes

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.testbuddy.com.testbuddy.messageBundleHandlers.TestMethodGenerationMessageBundleHandler

class CustomChecklistNode(
    override var description: String,
    override val element: PsiElement? = null,
    override var checked: Int = 0
) : TestingChecklistLeafNode(description, element) {
    override fun generateTestMethod(project: Project): PsiMethod {
        val methodName = TestMethodGenerationMessageBundleHandler.message("customNodeTestCaseName")
        return super.generateTestMethod(project, methodName)
    }
}
