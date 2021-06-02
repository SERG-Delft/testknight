package com.testbuddy.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class ActionData(val actionId: String) {
    private val dateTime: String = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())

    fun toHashString(): String {
        return actionId + dateTime
    }
}
