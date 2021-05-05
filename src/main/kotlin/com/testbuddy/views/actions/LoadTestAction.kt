package com.testbuddy.views.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPanelWithEmptyText
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBViewport
import com.intellij.ui.content.impl.ContentImpl
import java.awt.Dimension
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.Icon
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JTabbedPane

class LoadTestAction : AnAction() {

    /**
     * Updates the CopyPaste tab to add new test cases.
     * Has suppression for current skeleton code.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    @Suppress("MagicNumber")
    override fun actionPerformed(event: AnActionEvent) {

        val window: ToolWindow? = ToolWindowManager.getInstance(event.project!!).getToolWindow("TestBuddy")

        val tabbedPane = (
            (window!!.contentManager.contents[0] as ContentImpl)
                .component as JTabbedPane
            )
        val copyPasteTab = tabbedPane.getComponentAt(0) as JBPanelWithEmptyText
        val copyPasteScroll = copyPasteTab.getComponent(1) as JBScrollPane
        val copyPasteViewport = copyPasteScroll.getComponent(0) as JBViewport
        val copyPastePanel = copyPasteViewport.getComponent(0) as JPanel

        val mPanel = JPanel()

        mPanel.layout = BoxLayout(mPanel, BoxLayout.LINE_AXIS)

        // Create the labels and buttons.
        // The glue adds spacing/moves the button to the right
        mPanel.add(JBLabel("Test case 0"))
        mPanel.add(Box.createHorizontalGlue())
        mPanel.add(JButton("Copy"))
        mPanel.add(JButton("Goto"))

        // Limit size of the panel
        mPanel.minimumSize = Dimension(0, 50)
        mPanel.maximumSize = Dimension(Integer.MAX_VALUE, 50)
        mPanel.setSize(-1, 50)

        copyPastePanel.add(mPanel)
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
}
