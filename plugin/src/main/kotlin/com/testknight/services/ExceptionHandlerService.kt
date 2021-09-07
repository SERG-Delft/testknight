package com.testknight.services

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.testknight.exceptions.TestKnightException

class ExceptionHandlerService(private val project: Project) {

    /**
     * Display a notification with the provided title and content.
     * The type will represents the way that notification should be displayed: Warning, Error and Information.
     *
     * @param title String which represents the title of the Notification
     * @param content String which represents the content of the Notification
     * @param type String which represents the Icon type (WARNING, ERROR and INFORMATION)
     */
    fun notify(title: String, content: String, type: NotificationType) {

        NotificationGroupManager.getInstance()
            .getNotificationGroup("testknight.notifications")
            .createNotification(title, content, type)
            .notify(project)
    }

    /**
     * Display a notification with the provided title and content.
     * The type will represents the way that notification should be displayed: Warning, Error and Information.
     *
     * @param exception the TestKnightException for which the user should be notified
     */
    fun notify(exception: TestKnightException) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("testknight.notifications")
            .createNotification(exception.title, exception.content, exception.type)
            .notify(project)
    }
}
