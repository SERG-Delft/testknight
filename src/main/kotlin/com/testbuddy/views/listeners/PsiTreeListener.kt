package com.testbuddy.views.listeners

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.impl.source.PsiMethodImpl
import com.testbuddy.services.TestAnalyzerService
import com.testbuddy.utilities.UserInterfaceHelper

class PsiTreeListener(val project: Project) : PsiTreeChangeAdapter() {
    private val testAnalyzer = TestAnalyzerService()

    override fun childAdded(event: PsiTreeChangeEvent) {
        if(checkElement(event.child)) {
            UserInterfaceHelper.refreshTestCaseUI(project)
        }
    }

    override fun childRemoved(event: PsiTreeChangeEvent) {
        if(checkElement(event.parent)) {
            UserInterfaceHelper.refreshTestCaseUI(project)
        } else if(event.child != null && event.child is PsiMethodImpl) {
            // testAnalyzer.isTestMethod(event.child as PsiMethodImpl) seems to throw exception.
            // So currently, this will be called even if removed method is not a test method.
            UserInterfaceHelper.refreshTestCaseUI(project)
        }
    }
    override fun childReplaced(event: PsiTreeChangeEvent) {
        if(checkElement(event.parent)) {
            UserInterfaceHelper.refreshTestCaseUI(project)
        } else if(checkElement(event.child)) {
            UserInterfaceHelper.refreshTestCaseUI(project)
        }
    }
    override fun childMoved(event: PsiTreeChangeEvent) {
        if(checkElement(event.child)) {
            UserInterfaceHelper.refreshTestCaseUI(project)
        }
    }

    fun checkElement(element: PsiElement) : Boolean {
        return (element is PsiMethodImpl && testAnalyzer.isTestMethod(element as PsiMethodImpl))
    }
}
