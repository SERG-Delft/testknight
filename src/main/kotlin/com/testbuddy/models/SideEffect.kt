package com.testbuddy.models

import com.testbuddy.messageBundleHandlers.AssertionSuggestionMessageBundleHandler

abstract class SideEffect(open val info: String) {
    abstract fun toAssertionSuggestion(): AssertionSuggestion
}

abstract class ClassFieldMutationSideEffect(override val info: String) : SideEffect(info)
data class ReassignsClassFieldSideEffect(val nameOfField: String) : ClassFieldMutationSideEffect(nameOfField) {
    override fun toAssertionSuggestion(): AssertionSuggestion {
        return AssertionSuggestion(AssertionSuggestionMessageBundleHandler.message("fieldReassignment", nameOfField))
    }
}

data class MethodCallOnClassFieldSideEffect(val nameOfField: String, val nameOfMethod: String) :
    ClassFieldMutationSideEffect(nameOfField) {
    override fun toAssertionSuggestion(): AssertionSuggestion {
        return AssertionSuggestion(
            AssertionSuggestionMessageBundleHandler
                .message(
                    "methodFieldModification",
                    nameOfMethod, nameOfField
                )
        )
    }
}

data class MethodCallOnParameterSideEffect(val nameOfParameter: String, val nameOfMethod: String) :
    SideEffect(nameOfParameter) {
    override fun toAssertionSuggestion(): AssertionSuggestion {
        return AssertionSuggestion(
            AssertionSuggestionMessageBundleHandler
                .message(
                    "methodParameterModification",
                    nameOfMethod, nameOfParameter
                )
        )
    }
}
