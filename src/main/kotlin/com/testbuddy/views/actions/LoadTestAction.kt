package com.testbuddy.views.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBViewport
import com.intellij.ui.content.impl.ContentImpl
import com.testbuddy.models.TestMethodData
import com.testbuddy.services.DuplicateTestsService
import com.testbuddy.services.LoadTestsService
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTabbedPane

class LoadTestAction : AnAction() {

    /**
     * Updates the CopyPaste tab to load the test cases from the selected file.
     * Has suppression for current skeleton code.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    @Suppress("MagicNumber")
    override fun actionPerformed(event: AnActionEvent) {

        // Take the load test service and use the get tests

        val loadTestsService = event.project!!.service<LoadTestsService>()
        val listMethods = loadTestsService.getTests(event.getData(CommonDataKeys.PSI_FILE)!!)

        val window: ToolWindow? = ToolWindowManager.getInstance(event.project!!).getToolWindow("TestBuddy")

        val tabbedPane = (
            (window!!.contentManager.contents[0] as ContentImpl)
                .component as JTabbedPane
            )
        val copyPasteTab = tabbedPane.getComponentAt(0) as JBPanelWithEmptyText
        val copyPasteScroll = copyPasteTab.getComponent(1) as JBScrollPane
        val copyPasteViewport = copyPasteScroll.getComponent(0) as JBViewport
        val copyPastePanel = copyPasteViewport.getComponent(0) as JPanel

        copyPastePanel.removeAll()

        for (method in listMethods) {

            // Create the panel for each test method

            val mPanel = JPanel()

            mPanel.layout = BoxLayout(mPanel, BoxLayout.LINE_AXIS)

            // Create the labels and buttons.
            // The glue adds spacing/moves the button to the right

            mPanel.add(JBLabel(method.name))
            mPanel.add(Box.createHorizontalGlue())

            val copyButton = JButton("Copy")
            copyButton.addActionListener(ButtonCopyClickListener(method, event))

            mPanel.add(copyButton)
            mPanel.add(JButton("Goto"))
            // Limit size of the panel
            mPanel.minimumSize = Dimension(0, 50)
            mPanel.maximumSize = Dimension(Integer.MAX_VALUE, 50)
            mPanel.setSize(-1, 50)
            copyPastePanel.add(mPanel)
        }
    }

    /**
     * Determines whether this menu item is available for the current context.
     * Requires a project to be open.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    override fun update(e: AnActionEvent) {
        // Set the availability based on whether a project is open
        val project = e.project
        e.presentation.isEnabledAndVisible = project != null
    }

    /**
     * Inner class which represents the Listener for the Copy Button
     */
    private inner class ButtonCopyClickListener : ActionListener {
        private val reference: TestMethodData
        private val event: AnActionEvent

        /**
         * Constructor of the listener, which will store the event of the TestMethodData.
         *
         * @param reference represents a reference of the chosen Test -> TestMethodData
         * @param event Event received when a test method is chosen.
         */
        constructor(reference: TestMethodData, event: AnActionEvent) {
            this.reference = reference
            this.event = event
        }

        /**
         * Creates a Copy of the chosen test and append it on the final part of the code.
         *
         * @param e Event received when the Copy Button of a specific method is used
         */
        override fun actionPerformed(e: ActionEvent) {
            val duplicateTestsService = event.project!!.service<DuplicateTestsService>()
            val psiFile = event.getData(CommonDataKeys.PSI_FILE)
            val project = event.getData(CommonDataKeys.PROJECT)
            val editor = event.getData(CommonDataKeys.EDITOR)
            if (psiFile != null && editor != null && project != null) {
                duplicateTestsService.addToPsi(psiFile, editor, project)
            }
        }
    }
}
