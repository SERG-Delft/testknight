package com.testbuddy.utilities

import com.intellij.psi.PsiElement
import com.intellij.util.xmlb.Converter

class PsiConverter : Converter<PsiElement>() {
    override fun toString(value: PsiElement): String? {
        return "null"
    }

    override fun fromString(value: String): PsiElement? {
        return null
    }
}
