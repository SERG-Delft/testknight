package com.testknight.services

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.testknight.exceptions.TestKnightException

class ExceptionHandlerService(private val project: Project) {

    private val group = NotificationGroup.toolWindowGroup(
        "testknight.notifications.toolWindow",
        "TestKnight",
        true
    )

    /**
     * Display a notification with the provided title and content.
     * The type will represents the way that notification should be displayed: Warning, Error and Information.
     *
     * @param title String which represents the title of the Notification
     * @param content String which represents the content of the Notification
     * @param type String which represents the Icon type (WARNING, ERROR and INFORMATION)
     */
    fun notify(title: String, content: String, type: NotificationType) {

        val notification = group.createNotification(title, content, type)
        notification.notify(project)
    }

    /**
     * Display a notification with the provided title and content.
     * The type will represents the way that notification should be displayed: Warning, Error and Information.
     *
     * @param exception the TestKnightException for which the user should be notified
     */
    fun notify(exception: TestKnightException) {
        val notification = group.createNotification(exception.title, exception.content, exception.type)
        notification.notify(project)
    }
}
