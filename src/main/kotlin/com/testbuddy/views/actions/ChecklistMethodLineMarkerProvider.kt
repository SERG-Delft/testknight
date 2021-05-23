package com.testbuddy.views.actions

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiMethod
import com.testbuddy.services.TestAnalyzerService
import com.testbuddy.views.listeners.MethodChecklistIconHandler

/**
 * The Line Marker which is on the same line as the method declaration
 */
class ChecklistMethodLineMarkerProvider : LineMarkerProvider {

    private val testAnalyzerService = TestAnalyzerService()

    /**
     * The method which creates the Line Marker for the method
     * @param element PsiElement for which we have to build the Line Marker
     */
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {

        if (element is PsiIdentifier && element.parent is PsiMethod &&
            !testAnalyzerService.isTestMethod(element.parent as PsiMethod)
        ) {
            return LineMarkerInfo(
                element, element.textRange, AllIcons.General.Add,
                { element: PsiElement ->
                    buildString {
                        append("Generate checklist")
                    }
                },
                MethodChecklistIconHandler(), GutterIconRenderer.Alignment.RIGHT
            )
        }
        return null
    }
}
