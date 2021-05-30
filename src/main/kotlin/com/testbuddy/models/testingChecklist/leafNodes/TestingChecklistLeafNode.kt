package com.testbuddy.models.testingChecklist.leafNodes

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiType
import com.testbuddy.messageBundleHandlers.TestMethodGenerationMessageBundleHandler
import com.testbuddy.models.TestingChecklistNode

abstract class TestingChecklistLeafNode(
    open var description: String,
    open val element: PsiElement?,
    override var checked: Int = 0
) : TestingChecklistNode() {
    /**
     * Generate a test method given a project.
     * Should be implemented by subclasses.
     *
     * @param project the current project.
     * @return the PsiMethod representing the test.
     */
    abstract fun generateTestMethod(project: Project): PsiMethod

    /**
     * Generate a test method given a project and a name.
     * Can be used by subclasses in combination with the abstract
     * generateTestMethod.
     *
     * @param project the current project.
     * @param methodName the name of the method to generate.
     * @return the PsiMethod representing the test.
     */
    protected fun generateTestMethod(project: Project, methodName: String): PsiMethod {
        val factory = JavaPsiFacade.getInstance(project).elementFactory
        val method = factory.createMethod(methodName, PsiType.VOID)
        val comment = factory.createDocCommentFromText(
            "/** ${
            TestMethodGenerationMessageBundleHandler.message(
                "testMethodComment",
                this.description
            )
            } **/"
        )
        method.modifierList.addAnnotation("Test")
        WriteCommandAction.runWriteCommandAction(project) { method.body!!.add(comment) }
        return method
    }
}
