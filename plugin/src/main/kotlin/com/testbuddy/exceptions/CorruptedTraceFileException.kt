package com.testbuddy.exceptions

import com.intellij.notification.NotificationType

class CorruptedTraceFileException(ex: Exception) : TestBuddyException() {

    override var content: String = ex.message.toString()
    override var title: String = "Failed to read trace file"
    override var type: NotificationType = NotificationType.ERROR
}
