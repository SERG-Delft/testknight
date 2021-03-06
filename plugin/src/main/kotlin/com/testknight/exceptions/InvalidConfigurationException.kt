package com.testknight.exceptions

import com.intellij.notification.NotificationType

class InvalidConfigurationException(private val property: String, private val invalidConfiguration: String) :
    TestKnightException() {
    override var title: String = "Invalid Configuration"
    override var content: String = "Property $property cannot be set to $invalidConfiguration"
    override var type: NotificationType = NotificationType.ERROR
}
