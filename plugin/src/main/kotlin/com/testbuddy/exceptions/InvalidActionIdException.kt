package com.testbuddy.exceptions

import com.intellij.notification.NotificationType

class InvalidActionIdException(actionId: String) : TestBuddyException() {
    override var title: String = "Invalid Action ID"
    override var content: String = "$actionId is not a valid action ID"
    override var type: NotificationType = NotificationType.ERROR
}
