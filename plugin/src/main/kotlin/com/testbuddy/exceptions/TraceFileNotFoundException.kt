package com.testbuddy.exceptions

import com.intellij.notification.NotificationType

class TraceFileNotFoundException : TestBuddyException() {
    override var content: String = "Could not find coverage data"
    override var title: String = "Trace File not found:"
    override var type: NotificationType = NotificationType.INFORMATION
}
