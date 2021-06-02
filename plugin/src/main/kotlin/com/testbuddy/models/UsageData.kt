package com.testbuddy.models

import com.testbuddy.settings.SettingsService
import kotlinx.serialization.Serializable
import org.apache.commons.codec.digest.DigestUtils

@Serializable
data class UsageData(val actionsRecorded: List<ActionData>) {
    val userId: String = SettingsService.instance.state.userId
    val hash: String = DigestUtils.md5Hex(toHashString() + "exampleMagicString")

    fun toHashString(): String {
        val builder = StringBuilder()
        builder.append(userId)
        for (actionEventDto in actionsRecorded) {
            builder.append(actionEventDto.toHashString())
        }
        return builder.toString()
    }
}
