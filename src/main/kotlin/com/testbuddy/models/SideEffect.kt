package com.testbuddy.com.testbuddy.models

abstract class SideEffect

data class ThrowsExceptionSideEffect(val nameOfException: String) : SideEffect()
data class MutatesClassFieldSideEffect(val nameOfField: String) : SideEffect()
