package com.testbuddy.actions

import java.awt.event.ActionEvent
import javax.swing.AbstractAction

class NextInstallAction : AbstractAction("Use plugin") {

    /**
     * Invoked when an action occurs.
     * @param e the event to be processed
     */
    override fun actionPerformed(e: ActionEvent?) {
        println("is pressed")
        val source = e?.source ?: return
        println(source)
    }
}
