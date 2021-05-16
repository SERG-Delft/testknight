package com.testbuddy.views.actions

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.testbuddy.views.listeners.ClassChecklistIconHandler

/**
 * The Line Marker which is on the same line as the class declaration
 */
class ChecklistClassLineMarkerProvider : LineMarkerProvider {

    /**
     * The method which creates the Line Marker for the class
     * @param element PsiElement for which we have to build the Line Marker
     */
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {

        if (element is PsiIdentifier && element.parent is PsiClass) {
            return LineMarkerInfo(
                element, element.textRange, AllIcons.General.Add, null,
                ClassChecklistIconHandler(), GutterIconRenderer.Alignment.RIGHT
            )
        }
        return null
    }
}
