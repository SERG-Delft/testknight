package com.testbuddy.models.testingChecklist.leafNodes

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.testbuddy.messageBundleHandlers.TestMethodGenerationMessageBundleHandler

data class ConditionChecklistNode(
    override var description: String,
    override val element: PsiElement
) : TestingChecklistLeafNode(description, element) {
    /**
     * Generate a test method for a  condition statement.
     *
     * @param project the project.
     * @return the PsiMethod representing the test.
     */
    override fun generateTestMethod(project: Project): PsiMethod {
        val methodName = TestMethodGenerationMessageBundleHandler.message("conditionTestCaseName")
        return super.generateTestMethod(project, methodName)
    }
}
