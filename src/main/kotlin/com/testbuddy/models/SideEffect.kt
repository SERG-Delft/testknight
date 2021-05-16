package com.testbuddy.models

import com.testbuddy.com.testbuddy.models.AssertionSuggestion

abstract class SideEffect(open val info: String) {
    abstract fun toAssertionSuggestion(): AssertionSuggestion
}

abstract class ClassFieldMutationSideEffect(override val info: String) : SideEffect(info)
data class ReassignsClassFieldSideEffect(val nameOfField: String) : ClassFieldMutationSideEffect(nameOfField) {
    override fun toAssertionSuggestion(): AssertionSuggestion {
        return AssertionSuggestion("Assert that \"$nameOfField\" is re-assigned properly.")
    }
}

data class MethodCallOnClassFieldSideEffect(val nameOfField: String, val nameOfMethod: String) :
    ClassFieldMutationSideEffect(nameOfField) {
    override fun toAssertionSuggestion(): AssertionSuggestion {
        return AssertionSuggestion(
            "Assert that method \"$nameOfMethod\" " +
                "modifies field \"$nameOfField\" properly."
        )
    }
}

data class MethodCallOnParameterSideEffect(val nameOfParameter: String, val nameOfMethod: String) :
    SideEffect(nameOfParameter) {
    override fun toAssertionSuggestion(): AssertionSuggestion {
        return AssertionSuggestion(
            "Assert that method \"$nameOfMethod\" " +
                "modifies parameter \"$nameOfParameter\" properly."
        )
    }
}
