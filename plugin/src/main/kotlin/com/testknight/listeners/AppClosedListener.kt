package com.testknight.listeners

import com.intellij.ide.AppLifecycleListener
import com.testknight.services.UsageDataService

class AppClosedListener : AppLifecycleListener {

    override fun appWillBeClosed(isRestart: Boolean) = UsageDataService.instance.sendUserData()
}
