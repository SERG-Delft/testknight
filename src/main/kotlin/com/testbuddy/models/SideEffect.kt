package com.testbuddy.models

open class SideEffect(val info: String)

data class ThrowsExceptionSideEffect(val nameOfException: String) : SideEffect(nameOfException)
data class MutatesClassFieldSideEffect(val nameOfField: String) : SideEffect(nameOfField)
