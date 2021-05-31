package com.testbuddy.models.testingChecklist.leafNodes

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiThrowStatement
import com.testbuddy.messageBundleHandlers.TestMethodGenerationMessageBundleHandler

data class ThrowStatementChecklistNode(
    override var description: String,
    override val element: PsiThrowStatement,
    val nameOfException: String
) : TestingChecklistLeafNode(description, element) {

    /**
     * Generate a test method for a throw statement.
     *
     * @param project the project.
     * @return the PsiMethod representing the test.
     */
    override fun generateTestMethod(project: Project): PsiMethod {
        val methodName = TestMethodGenerationMessageBundleHandler.message("throwTestCaseName")
        return super.generateTestMethod(project, methodName)
    }
}
