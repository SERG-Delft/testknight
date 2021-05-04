package com.testbuddy.com.testbuddy.models

abstract class SideEffect
data class ThrowsExceptionSideEffect(val nameOfException: String) : SideEffect()
data class ModifiesClassFieldSideEffect(val nameOfField: String) : SideEffect()
data class MutatesArgumentSideEffect(val nameOfArgument: String) : SideEffect()
// data class PerformsIOSideEffect(...) NOTE: we might need this later
