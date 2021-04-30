package com.testbuddy.views

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.layout.panel
import java.awt.Component
import java.awt.Dimension
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel

class UserInterface {

    private var mainUI: JBTabbedPane? = null
    private var addTest: JButton? = null
    private var testCaseUI: DialogPanel? = null

    /**
     * Returns the Component which will be displayed on the tool window
     */
    fun getContent(): JBTabbedPane? {
        return mainUI
    }

    /**
     * Skeleton code which returns the base scrollable panel on which we will add the other components.
     */
    fun getCopyPasteTab(): Component? {
        val panel = JBScrollPane()

        // Temporary example to show how adding new test cases works.
        testCaseUI = panel {
            row {
                addTest?.let { it() }
            }
        }

        panel.setViewportView(testCaseUI)

        testCaseUI!!.layout = BoxLayout(testCaseUI, BoxLayout.PAGE_AXIS)

        return panel
    }

    /**
     * Skeleton code which adds a new component (panel with label and buttons) onto the copy paste Tab.
     * Will be modified once communication with backend has been established.
     */
    @Suppress(MagicNumber)
    private fun updateUI() {

        val mPanel = JPanel()

        mPanel.layout = BoxLayout(mPanel, BoxLayout.LINE_AXIS)

        // Create the labels and buttons.
        // The glue adds spacing/moves the button to the right
        mPanel.add(JBLabel("Test case 0"))
        mPanel.add(Box.createHorizontalGlue())
        mPanel.add(JButton("Copy"))
        mPanel.add(JButton("Goto"))

        mPanel.minimumSize = Dimension(0, 50)
        mPanel.maximumSize = Dimension(Integer.MAX_VALUE, 50)
        mPanel.setSize(-1, 50)

        testCaseUI!!.add(mPanel)
    }

    // Constructor
    init {

        // This button is just to give an idea how adding new test cases would look like
        // This will be removed soon after communication with backend has been established.
        addTest = JButton("Add new test")
        addTest!!.addActionListener { updateUI() }

        mainUI = JBTabbedPane(JBTabbedPane.TOP, JBTabbedPane.SCROLL_TAB_LAYOUT)
        mainUI!!.addTab("CopyPaste", getCopyPasteTab())
    }
}
