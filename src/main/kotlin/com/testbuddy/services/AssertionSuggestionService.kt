package com.testbuddy.com.testbuddy.services

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.testbuddy.com.testbuddy.models.AssertionSuggestion
import com.testbuddy.com.testbuddy.utilities.StringFormatter
import com.testbuddy.services.MethodAnalyzerService
import com.testbuddy.services.TestAnalyzerService

class AssertionSuggestionService {

    private val testAnalyzerService = TestAnalyzerService()

    /**
     * Returns a list of assertion suggestions based on the given method.
     * Works iff the testMethod given is a test method.
     * If it is not, the method returns an empty list.
     *
     * @param testMethod the test method.
     * @param methodCall the method call to the method under test.
     * @param project the current project.
     * @return a list of AssertionSuggestions for the given method under test.
     */
    fun getAssertions(
        testMethod: PsiMethod,
        methodCall: PsiMethodCallExpression,
        project: Project
    ): List<AssertionSuggestion> {
        return if (testAnalyzerService.isTestMethod(testMethod)) {
            val methodAnalyzerService = project.service<MethodAnalyzerService>()
            val methodUnderTest = methodCall.resolveMethod() ?: return emptyList()
            this.getAssertions(methodUnderTest, methodAnalyzerService)
        } else {
            emptyList()
        }
    }

    /**
     * Append a list of assertion suggestions as a comment in the end
     * of the given method. Works iff the testMethod given is a test method.
     * If it is not, the method does nothing.
     *
     * @param testMethod the test method to generate on and append the assertions to.
     * @param methodCall the method call to the method under test.
     * @param project the current project.
     */
    @Suppress("ReturnCount")
    fun appendAssertionsAsComments(testMethod: PsiMethod, methodCall: PsiMethodCallExpression, project: Project) {
        if (testAnalyzerService.isTestMethod(testMethod)) {
            // Create the assertion suggestions.
            val methodAnalyzerService = project.service<MethodAnalyzerService>()
            val methodUnderTest = methodCall.resolveMethod() ?: return
            val assertions = this.getAssertions(methodUnderTest, methodAnalyzerService)
            if (assertions.isEmpty()) return

            // Create the message to be appended
            val builder = StringBuilder()
            builder.append("/**\n")
            assertions.forEach { builder.append("* ${it.message}\n") }
            builder.append("*/")

            // Create the PsiDocComment
            val factory = JavaPsiFacade.getInstance(project).elementFactory
            val comment = factory.createDocCommentFromText(builder.toString())
            val methodBody = testMethod.body ?: return
            WriteCommandAction.runWriteCommandAction(project) { methodBody.add(comment) }
        }
    }

    /**
     * Returns a list of assertion suggestions.
     *
     * @param methodUnderTest the method to analyze.
     * @param methodAnalyzerService the analyzer to be used to get the side-effects.
     * @return a list of AssertionSuggestion objects.
     */
    private fun getAssertions(
        methodUnderTest: PsiMethod,
        methodAnalyzerService: MethodAnalyzerService
    ): List<AssertionSuggestion> {
        val assertionsBasedOnSideEffects =
            methodAnalyzerService.getSideEffects(methodUnderTest).map { it.toAssertionSuggestion() }
        val assertionsBasedOnOutput = this.getAssertionOnOutput(methodUnderTest)
        return assertionsBasedOnOutput + assertionsBasedOnSideEffects
    }

    /**
     * Returns a list of assertions based on the output.
     * Currently there is only one suggestion but in the future,
     * we might want to add more. This is why a list has been set as
     * the return type.
     *
     * @param method the method to generate the assertions on.
     * @return a list of AssertionSuggestion objects.
     */
    private fun getAssertionOnOutput(method: PsiMethod): List<AssertionSuggestion> {
        val methodType = method.returnType?.getCanonicalText()
        if (methodType == null || methodType == "void") return emptyList()
        val methodName = StringFormatter.formatMethodName(method.name)
        return listOf(AssertionSuggestion("Assert that \"$methodName\" returns the proper \"$methodType\"."))
    }
}
