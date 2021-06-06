package com.testbuddy.listeners

import com.intellij.ide.AppLifecycleListener
import com.testbuddy.services.UsageDataService

class AppClosedListener : AppLifecycleListener {

    override fun appWillBeClosed(isRestart: Boolean) = UsageDataService.instance.sendUserData()
}
