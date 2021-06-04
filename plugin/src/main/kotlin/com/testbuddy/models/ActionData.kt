package com.testbuddy.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * This class represents a logged action.
 *
 * @param actionId the action logged
 */
data class ActionData(val actionId: String) {
    private val dateTime: String = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())

    fun toHashString(): String {
        return "$actionId$dateTime"
    }
}
