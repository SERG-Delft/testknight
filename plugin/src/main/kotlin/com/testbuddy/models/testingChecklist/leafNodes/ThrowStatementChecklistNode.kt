package com.testbuddy.models.testingChecklist.leafNodes

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.util.xmlb.annotations.OptionTag
import com.testbuddy.messageBundleHandlers.TestMethodGenerationMessageBundleHandler
import com.testbuddy.utilities.PsiConverter

data class ThrowStatementChecklistNode(
    override var description: String = "",
//    override val element: PsiThrowStatement,
    @OptionTag(converter = PsiConverter::class)
    override var element: PsiElement? = null,
    val nameOfException: String = ""
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
