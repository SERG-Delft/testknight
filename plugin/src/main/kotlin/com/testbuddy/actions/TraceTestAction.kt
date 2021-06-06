package com.testbuddy.actions

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.startOffset
import com.testbuddy.services.TestAnalyzerService
import com.testbuddy.services.TestTracingService
import com.testbuddy.services.UsageDataService
import javax.swing.Icon

class TraceTestAction : PsiElementBaseIntentionAction(), IntentionAction, Iconable {

    /**
     * If this action is applicable, then the "See lines covered" text will be shown in the dropdown menu.
     */
    override fun getText(): String {
        return "See lines covered"
    }

    /**
     * Returns text for name of this family of intentions.
     *
     * @return the intention family name.
     */
    override fun getFamilyName(): String {
        return "Trace test"
    }

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

        // get current test file information
        val parentMethod = PsiTreeUtil.getParentOfType(element, PsiMethod::class.java)
        val parentClass = PsiTreeUtil.getParentOfType(element, PsiClass::class.java)
        val methodCall = PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression::class.java)

        if (parentMethod == null || methodCall == null || parentClass == null) return

        // highlight the data
        val testName = "${parentClass.name},${parentMethod.name}"

        project.service<TestTracingService>().highlightTest(testName)
        // try catch for the  NoTestCoverageDataException no necessary

        // get method declaration information
        val methodDeclaration = methodCall.resolveMethod()
        val classDefined = PsiTreeUtil.getParentOfType(methodDeclaration, PsiClass::class.java)
        val fileDefined = PsiTreeUtil.getParentOfType(methodDeclaration, PsiFile::class.java)

        if (methodDeclaration == null || classDefined == null || fileDefined == null) return

        // get declaration descriptor
        val fileEditorManager = FileEditorManager.getInstance(project)
        val virtualFile = fileDefined.virtualFile
        val descriptor = OpenFileDescriptor(project, virtualFile, methodDeclaration.startOffset)

        // open method declaration
        fileEditorManager.openTextEditor(descriptor, true)
        UsageDataService.instance.logTraceTest()
    }

    /**
     * Returns the icon for the intention Action
     */
    override fun getIcon(flags: Int): Icon {
        return IconLoader.getIcon("/icons/pluginIcon.svg")
    }
}
