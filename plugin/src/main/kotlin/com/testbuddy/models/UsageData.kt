package com.testbuddy.models

import com.testbuddy.settings.SettingsService
import kotlinx.serialization.Serializable

@Serializable
data class UsageData(val actionsRecorded: List<ActionData>) {
    val userId = SettingsService.instance.state.userId
    val hash: String = "TODO"
}
