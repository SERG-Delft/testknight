package com.testbuddy.com.testbuddy.models

import com.intellij.psi.PsiElement

open class TestingChecklistItem(val description: String, open val relatedElement: PsiElement)
