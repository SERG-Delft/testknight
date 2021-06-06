package com.testbuddy.exceptions

import com.intellij.notification.NotificationType

class DocumentNotFoundException : TestBuddyException() {
    override var title: String = "Document not found"
    override var content: String = "Cannot found document for the file"
    override var type: NotificationType = NotificationType.ERROR
}
