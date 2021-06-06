package com.testbuddy.exceptions

import com.intellij.notification.NotificationType

class InvalidVirtualFileException(className: String, invalidReason: String) : TestBuddyException() {
    override var title: String = "Virtual file is invalid"
    override var content: String = "Invalid virtual file for class $className. Reason: $invalidReason"
    override var type: NotificationType = NotificationType.ERROR
}
