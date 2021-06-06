package com.testbuddy.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Represents a logged action.
 *
 * @param actionId the action logged
 */
data class ActionData(
    val actionId: String,
    val dateTime: String = DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now())
) {

    /**
     * Creates a string representation
     * of the DTO that is suitable for
     * hashing.
     *
     * @return the string that can be used
     * to generate a hash.
     */
    fun toHashString() = "$actionId$dateTime"
}
