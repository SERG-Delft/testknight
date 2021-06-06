package com.testbuddy.exceptions

import com.intellij.notification.NotificationType

class NoTestCoverageDataException : TestBuddyException() {
    override var title: String = "No Test Coverage Data"
    override var content: String = ""
    override var type: NotificationType = NotificationType.ERROR
}
