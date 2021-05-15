package com.testbuddy.com.testbuddy.models

import com.intellij.psi.PsiAssignmentExpression
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethodCallExpression

open class SideEffect(open val info: String)

open class ClassFieldMutationSideEffect(override val info: String): SideEffect(info)
data class ReassignsClassFieldSideEffect(val nameOfField: String) : ClassFieldMutationSideEffect(nameOfField)
data class MethodCallOnClassFieldSideEffect(val nameOfField: String, val nameOfMethod: String): ClassFieldMutationSideEffect(nameOfField)
data class MethodCallOnArgumentSideEffect(val nameOfArgument: String, val nameOfMethod: String): SideEffect(nameOfArgument)
