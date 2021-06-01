package com.testbuddy.models.testingChecklist.leafNodes.loopStatements

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.testbuddy.messageBundleHandlers.TestMethodGenerationMessageBundleHandler
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode

data class WhileStatementChecklistNode(
    override var description: String,
    override val element: PsiElement,
) : TestingChecklistLeafNode(description, element) {

    /**
     * Generate a test method for a while loop statement.
     *
     * @param project the project.
     * @return the PsiMethod representing the test.
     */
    override fun generateTestMethod(project: Project): PsiMethod {
        val methodName = TestMethodGenerationMessageBundleHandler.message("whileLoopTestCaseName")
        return super.generateTestMethod(project, methodName)
    }
}
