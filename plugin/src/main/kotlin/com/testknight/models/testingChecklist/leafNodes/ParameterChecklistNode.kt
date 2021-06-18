package com.testknight.models.testingChecklist.leafNodes

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.util.xmlb.annotations.OptionTag
import com.testknight.messageBundleHandlers.TestMethodGenerationMessageBundleHandler
import com.testknight.utilities.PsiConverter

data class ParameterChecklistNode(
    override var description: String = "",
//    override var element: PsiParameter?,
    @OptionTag(converter = PsiConverter::class)
    override var element: PsiElement? = null,
    val parameterName: String = "",
    val suggestedValue: String = ""
) : TestingChecklistLeafNode(description, element) {
    /**
     * Generate a test method for a parameter.
     *
     * @param project the project.
     * @return the PsiMethod representing the test.
     */
    override fun generateTestMethod(project: Project): PsiMethod {
        val methodName = TestMethodGenerationMessageBundleHandler.message("parameterTestCaseName")
        return super.generateTestMethod(project, methodName)
    }
}
