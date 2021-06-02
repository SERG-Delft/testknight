package com.testbuddy.listeners

import com.github.kittinunf.fuel.core.isSuccessful
import com.intellij.ide.AppLifecycleListener
import com.testbuddy.services.UsageDataService
import com.testbuddy.settings.SettingsService

class AppClosedListener : AppLifecycleListener {

    override fun appWillBeClosed(isRestart: Boolean) {
        if (SettingsService.instance.state.telemetrySettings.isEnabled) {

            val (req, result, response) = UsageDataService.instance.sendUserData()

            if (result.isSuccessful) {
                println("Usage Data sent succesfully")
            } else {
                println("Failed to send usage data: ${result.statusCode}")
            }
        }
    }
}
