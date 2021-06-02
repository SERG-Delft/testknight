package com.testbuddy.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class ActionData(val actionId: String) {
    val datetime: String = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())
}
