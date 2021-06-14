package com.testbuddy.exceptions

import com.intellij.notification.NotificationType

/**
 * Invalid tree path exception.
 * Doesn't need to be notified to the user (In case ParameterSuggestion tree throws this exception).
 */
class InvalidTreePathException(reason: String) : TestBuddyException() {
    override var title: String = "Invalid Tree Path"
    override var content: String = "Reason: $reason"
    override var type: NotificationType = NotificationType.ERROR
}
