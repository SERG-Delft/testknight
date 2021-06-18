package com.testknight.exceptions

import com.intellij.notification.NotificationType

class NoTestCoverageDataException : TestKnightException() {
    override var title: String = "No Test Coverage Data"
    override var content: String = ""
    override var type: NotificationType = NotificationType.ERROR
}
