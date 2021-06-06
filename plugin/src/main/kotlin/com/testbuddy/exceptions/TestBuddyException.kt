package com.testbuddy.exceptions

import com.intellij.notification.NotificationType

abstract class TestBuddyException : Exception() {
    abstract var title: String
    abstract var content: String
    abstract var type: NotificationType
}
