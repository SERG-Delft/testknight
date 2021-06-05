package com.testbuddy.models

import com.testbuddy.messageBundleHandlers.ServerMessageBundleHandler
import com.testbuddy.settings.SettingsService
import org.apache.commons.codec.digest.DigestUtils

/**
 * Represents a session usage data
 *
 * @param actionsRecorded the list of actions to include in the usageData
 */
data class UsageData(val actionsRecorded: List<ActionData>) {

    // userId is read from the settings. A random userId is made on plugin install
    val userId: String = SettingsService.instance.state.userId

    // the hash is used to verify the request.
    val hash: String = DigestUtils.md5Hex("${toHashString()}${ServerMessageBundleHandler.message("hashString")}")

    /**
     * Creates a string representation
     * of the DTO that is suitable for
     * hashing.
     *
     * @return the string that can be used
     * to generate a hash.
     */
    fun toHashString(): String {
        val builder = StringBuilder()
        builder.append(userId)
        for (actionEventDto in actionsRecorded) {
            builder.append(actionEventDto.toHashString())
        }
        return builder.toString()
    }
}
