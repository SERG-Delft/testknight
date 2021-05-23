package com.testbuddy.models

/**
 * Represents an element to be highlighted when a test is duplicated
 *
 * @param startOffset the start position of the element in the source code
 * @param endOffset the end position of the element in the source code
 * @param text the text
 */
data class HighlightedTextData(val startOffset: Int, val endOffset: Int, val text: String)
