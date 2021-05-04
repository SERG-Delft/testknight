package com.testbuddy.com.testbuddy.models

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.RangeHighlighter
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import java.awt.Color

/**
 * Represents an element in source code whose highlighting can be toggled on and off
 *
 * @param psiElement the PSI element
 * @param editor the text editor the element belongs to
 */
class HighlightableElement(val psiElement: PsiElement, val editor: Editor) {

    private var highlighted = false
    private val textAttributes: TextAttributes = TextAttributes()
    private var highlighter: RangeHighlighter? = null

    init {
        textAttributes.backgroundColor = Color.CYAN
    }

    /**
     * Highlights the element.
     */
    fun highlight() {
        highlighter = editor.markupModel.addRangeHighlighter(
            psiElement.startOffset,
            psiElement.endOffset,
            0,
            textAttributes,
            HighlighterTargetArea.EXACT_RANGE
        )

        highlighted = true
    }

    /**
     * Removes the highlighting from the element.
     */
    fun removeHighlight() {
        editor.markupModel.removeHighlighter(highlighter!!)
        highlighted = false
    }

    fun isHighlighted(): Boolean {
        return highlighted
    }

    override fun toString(): String {
        return "HighlightableElement(highlighted=$highlighted, psiElement=$psiElement, editor=$editor)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HighlightableElement

        if (highlighted != other.highlighted) return false
        if (psiElement != other.psiElement) return false
        if (editor != other.editor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = highlighted.hashCode()
        result = 31 * result + psiElement.hashCode()
        result = 31 * result + editor.hashCode()
        return result
    }
}
