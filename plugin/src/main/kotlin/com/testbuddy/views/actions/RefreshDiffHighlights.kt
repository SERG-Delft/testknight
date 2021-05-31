package com.testbuddy.com.testbuddy.views.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.psi.PsiClass
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.com.testbuddy.services.CoverageHighlighterService

class RefreshDiffHighlights : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        val className = PsiTreeUtil.findChildOfType(psiFile, PsiClass::class.java)?.name

        if (project == null || editor == null || className == null) return

        val coverageHighlighterService = project.service<CoverageHighlighterService>()

        coverageHighlighterService.refreshHighlights(editor, className)
    }
}
