package com.testbuddy.models

import com.testbuddy.settings.SettingsService

class UsageData(val actionsRecorded: List<ActionData>) {
    val userId = SettingsService.instance.state.userId
    val hash: String = "TODO"
}
