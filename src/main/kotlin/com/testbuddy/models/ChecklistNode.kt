package com.testbuddy.models

import com.intellij.psi.PsiElement

class ChecklistNode(var description: String, val element: PsiElement, var checkCount: Int, val depth: Int, val index: Int)
