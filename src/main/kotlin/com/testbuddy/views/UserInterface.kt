package com.testbuddy.views

import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBTabbedPane



class UserInterface(toolWindow: ToolWindow) {

    private var mainUI: JBTabbedPane? = null

    fun getContent(): JBTabbedPane? {
        return mainUI
    }

    //Constructor
    init {
        mainUI = JBTabbedPane()

        //Function call which returns the tab for copy paste
        //i.e. mainUI.addTab(getCopyPasteTab())

        //Function call which returns the tab for checklist
    }
}