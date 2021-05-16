package com.testbuddy.com.testbuddy.models

open class SideEffect(open val info: String)

open class ClassFieldMutationSideEffect(override val info: String) : SideEffect(info)
data class ReassignsClassFieldSideEffect(val nameOfField: String) : ClassFieldMutationSideEffect(nameOfField)
data class MethodCallOnClassFieldSideEffect(val nameOfField: String, val nameOfMethod: String) :
    ClassFieldMutationSideEffect(nameOfField)

data class MethodCallOnArgumentSideEffect(val nameOfArgument: String, val nameOfMethod: String) :
    SideEffect(nameOfArgument)
