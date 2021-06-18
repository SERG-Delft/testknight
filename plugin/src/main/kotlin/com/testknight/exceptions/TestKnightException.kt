package com.testknight.exceptions

import com.intellij.notification.NotificationType

abstract class TestKnightException : Exception() {
    abstract var title: String
    abstract var content: String
    abstract var type: NotificationType
}
