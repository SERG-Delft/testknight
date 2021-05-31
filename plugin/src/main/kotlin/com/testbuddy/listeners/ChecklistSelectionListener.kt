package com.testbuddy.listeners

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import com.intellij.ui.CheckedTreeNode
import com.testbuddy.models.ChecklistUserObject
import com.testbuddy.models.testingChecklist.TestingChecklistNode
import com.testbuddy.models.testingChecklist.leafNodes.TestingChecklistLeafNode
import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistClassNode
import com.testbuddy.models.testingChecklist.parentNodes.TestingChecklistMethodNode
import com.testbuddy.settings.SettingsService
import javax.swing.event.TreeSelectionEvent
import javax.swing.event.TreeSelectionListener

/**
 * Listener which highlights and goes to the PsiElement of the selected item in checklist tree.
 *
 * @param project current project.
 */
class ChecklistSelectionListener(val project: Project) : TreeSelectionListener {

    private var editor: Editor? = null
    private var highlighterList: MutableList<RangeHighlighter> = mutableListOf()

    private fun settingsState() = SettingsService.instance.state

    /**
     * The main listener function which gets called whenever the selection gets changed.
     *
     * Suppressing magic number because we need a "offset" number.
     *
     * It always removes all the the previously highlighted element.
     * Checks if the the current editor contains the selected PsiElement.
     * If it does, it highlights the PsiElement and moves to that PsiElement.
     *
     *@param event the tree selection event.
     */
    @SuppressWarnings("MagicNumber")
    override fun valueChanged(event: TreeSelectionEvent) {

        for (highlighter in highlighterList) {
            editor?.markupModel?.removeHighlighter(highlighter)
        }

        val textEditor = (FileEditorManager.getInstance(project).selectedEditor as TextEditor?) ?: return
        editor = textEditor.editor

        val component = (event.newLeadSelectionPath ?: return).lastPathComponent ?: return
        if (component is CheckedTreeNode) {
            val userObject = (component.userObject as ChecklistUserObject).checklistNode

            val element = getElement(userObject) ?: return

            if (isElementInEditor(editor!!, element)) {

                val myKey = DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT_HIGHLIGHTED

                // Create highlight and add to the highlight list.
                if (settingsState().checklistSettings.highlightChecklistItem) {
                    highlighterList.add(
                        editor!!.markupModel.addRangeHighlighter(
                            myKey,
                            element.startOffset,
                            element.endOffset,
                            HighlighterLayer.SELECTION - 200,
                            HighlighterTargetArea.EXACT_RANGE
                        )
                    )
                }

                // Goto the offset
                if (settingsState().checklistSettings.gotoChecklistItem) {
                    editor!!.caretModel.primaryCaret.moveToOffset(element.startOffset)
                    editor!!.scrollingModel.scrollToCaret(ScrollType.CENTER)
                }
            }
        }
    }

    /**
     * Gets the PsiElement from TestingChecklistNode
     *
     * @param obj The TestingChecklistNode from which we want to extract the PsiElement,
     * @return the PsiElement of the node, null if it's not found.
     */
    private fun getElement(obj: TestingChecklistNode): PsiElement? {
        when (obj) {
            is TestingChecklistClassNode -> return obj.element
            is TestingChecklistMethodNode -> return obj.element
            is TestingChecklistLeafNode -> return obj.element
        }

        return null
    }

    /**
     * Checks if the editor has the PsiElement.
     *
     * @param editor current Editor
     * @param psiElement the psiElement we want to check.
     * @return true if psiElement is in the editor, false otherwise.
     */
    private fun isElementInEditor(editor: Editor, psiElement: PsiElement): Boolean {
        val elementFile = psiElement.containingFile.virtualFile ?: return false
        val editorFile = FileDocumentManager.getInstance().getFile(editor.document)
        return elementFile == editorFile
    }
}
