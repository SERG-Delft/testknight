package com.testbuddy.views.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.services.TestAnalyzerService

class TraceTestAction : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String = "See lines covered"

    override fun getFamilyName(): String = "Coverage"

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {

        val parentMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
        val methodCall = PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression::class.java)
        val testAnalyzerService = project.service<TestAnalyzerService>()

        return element is PsiIdentifier &&
            methodCall != null &&
            parentMethod != null &&
            testAnalyzerService.isTestMethod(parentMethod)
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val parentMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
        val methodCall = PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression::class.java)

        if (parentMethod == null || methodCall == null)  return

        val methodDeclaration = methodCall.resolveMethod()
    }
}
