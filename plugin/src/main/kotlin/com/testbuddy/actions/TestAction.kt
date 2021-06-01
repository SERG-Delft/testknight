package com.testbuddy.actions

import com.intellij.coverage.CoverageDataManager
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginInstaller
import com.intellij.ide.plugins.PluginStateListener
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.impl.DocumentMarkupModel
import com.intellij.openapi.editor.impl.DocumentMarkupModelManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import com.testbuddy.models.testingChecklist.leafNodes.branchingStatements.SwitchStatementChecklistNode
import com.testbuddy.services.TestMethodGenerationService
import com.testbuddy.settings.SettingsService
import com.testbuddy.settings.SettingsState
import java.util.UUID

// Temporary Test Action for debug purposes
class TestAction : AnAction() {

    @Suppress("UnusedPrivateMember")
    override fun actionPerformed(event: AnActionEvent) {
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)!!
        val editor = event.getData(CommonDataKeys.EDITOR)!!
        val project = event.getData(CommonDataKeys.PROJECT)!!
        val caret = editor.caretModel.primaryCaret
        val elementAtCaret = psiFile.findElementAt(caret.offset)
        val parentMethod = PsiTreeUtil.getParentOfType(elementAtCaret, PsiMethod::class.java)
        val covDataManager = CoverageDataManager.getInstance(project)
        val markupModel = DocumentMarkupModel.forDocument(editor.document, project, true)
        val docMarkupModelManager = DocumentMarkupModelManager.getInstance(project)
        val fileEditorManager = FileEditorManager.getInstance(project)
        val service = project.service<TestMethodGenerationService>()
        val checklistItem = SwitchStatementChecklistNode("This is the description", psiFile, "var", null)
        val settings = SettingsService.instance.state
    }
}
