package com.testknight.listeners

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.impl.source.PsiMethodImpl
import com.testknight.services.TestAnalyzerService
import com.testknight.utilities.UserInterfaceHelper

/**
 * PsiTreeListener which refreshes the test case UI whenever methods got modified in the editor
 *
 * overridden methods are:
 *      - childAdded
 *      - childRemoved
 *      - childReplaced
 *      - childMoved
 *
 * All the above mentioned methods check if the modified child is a method.
 * If it is, then the UI gets updated.
 */
class PsiTreeListener(val project: Project) : PsiTreeChangeAdapter() {
    private val testAnalyzer = TestAnalyzerService()

    override fun childAdded(event: PsiTreeChangeEvent) {
        if (checkElement(event.child)) {
            UserInterfaceHelper.refreshTestCaseUI(project)
        }
    }

    override fun childRemoved(event: PsiTreeChangeEvent) {
        if (checkElement(event.parent)) {
            UserInterfaceHelper.refreshTestCaseUI(project)
        } else if (event.child != null && event.child is PsiMethodImpl) {
            // testAnalyzer.isTestMethod(event.child as PsiMethodImpl) seems to throw exception.
            // So currently, this will be called even if removed method is not a test method.
            UserInterfaceHelper.refreshTestCaseUI(project)
        }
    }

    override fun childReplaced(event: PsiTreeChangeEvent) {
        if (checkElement(event.parent)) {
            UserInterfaceHelper.refreshTestCaseUI(project)
        } else if (checkElement(event.child)) {
            UserInterfaceHelper.refreshTestCaseUI(project)
        }
    }

    override fun childMoved(event: PsiTreeChangeEvent) {
        if (checkElement(event.child)) {
            UserInterfaceHelper.refreshTestCaseUI(project)
        }
    }

    /**
     * Checks if the PsiElement is a test method.
     *
     * @param element given psi element
     * @return true if the PsiElement is a test method, else false.
     */
    private fun checkElement(element: PsiElement): Boolean {
        return (element is PsiMethodImpl && testAnalyzer.isTestMethod(element))
    }
}
