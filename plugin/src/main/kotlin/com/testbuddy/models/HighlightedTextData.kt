package com.testbuddy.com.testbuddy.models

import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import com.testbuddy.com.testbuddy.settings.SettingsService

/**
 * Represents an element to be highlighted when a test is duplicated
 *
 * @param startOffset the start position of the element in the source code
 * @param endOffset the end position of the element in the source code
 * @param text the text
 */
class HighlightedTextData(var startOffset: Int, var endOffset: Int, var text: String) {

    /**
     * Construct it for a literal. This takes into account strings.
     *
     * @param element: The PsiLiteralExpression
     */
    constructor(element: PsiElement) : this(element.startOffset, element.endOffset, element.text) {

        // if literal is string, include only the text inside quotes
        if (isQuoteHlEnabled() && (isString(element) || isChar(element))) {
            this.startOffset = element.startOffset + 1
            this.endOffset = element.endOffset - 1
            this.text = element.text.substring(1, element.text.length - 1)
        }
    }

    /**
     * Returns true if the provided PSI element corresponds to a string literal.
     *
     * @param element the PSI element
     */
    private fun isString(element: PsiElement) =
        element is PsiLiteralExpression && element.text.matches(Regex("\".*\""))

    /**
     * Returns true if the provided PSI element corresponds to a string literal.
     *
     * @param element the PSI element
     */
    private fun isChar(element: PsiElement) =
        element is PsiLiteralExpression && element.text.matches(Regex("'.'"))

    /**
     * Reads the settings to determine if in-quote highlighting is enabled.
     *
     * @return true if in-quote highlighting is enabled
     */
    private fun isQuoteHlEnabled() = ApplicationManager
        .getApplication()
        .getService(SettingsService::class.java)
        .state.testListSettings.highlightStrategies["Highlight string inside quotes"]!!
}
