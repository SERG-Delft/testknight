package com.testbuddy.views

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.CheckboxTree
import com.intellij.ui.CheckboxTree.CheckboxTreeCellRenderer
import com.intellij.ui.CheckedTreeNode
import com.intellij.ui.SimpleTextAttributes
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.layout.panel
import com.intellij.util.ui.tree.TreeUtil
import java.awt.Component
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JTree

class UserInterface {

    private var mainUI: JBTabbedPane? = null
    private var testCaseUI: DialogPanel? = null

    /**
     * Returns the Component which will be displayed on the tool window
     */
    fun getContent(): JBTabbedPane? {
        return mainUI
    }

    private fun createCheckBox(name: String, action: String): JBCheckBox {
        val x = JBCheckBox(name)
        x.addActionListener {
            run {
                if (x.isSelected) {
                    println(action)
                }
            }
        }
        return x
    }

    /**
     * Temporary suppress because skeleton code doesn't use the attributes.
     *
     * Create a new CheckboxTree everytime to add new checklists.
     * Currently this has skeleton code for CellRenderer and other features which needs to be modified later.
     *
     */
    @Suppress("UnusedPrivateMember")
    private fun createCheckListMethod(nameMethod: String, checkBoxes: List<JBCheckBox>): Component {
        val list: MutableList<JBCheckBox> = mutableListOf()
        list.add(createCheckBox("Item 1", "It works"))
        list.add(createCheckBox("Item 2", "It works2"))

        val root = CheckedTreeNode("root")

        val tree = CheckboxTree(
            object : CheckboxTreeCellRenderer(true, false) {
                override fun customizeRenderer(
                    tree: JTree,
                    value: Any,
                    selected: Boolean,
                    expanded: Boolean,
                    leaf: Boolean,
                    row: Int,
                    hasFocus: Boolean
                ) {
                    if (value !is CheckedTreeNode) return
                    val text: String = value.userObject as String
                    val renderer = textRenderer

                    renderer.icon = AllIcons.General.Modified
                    renderer.append(text, SimpleTextAttributes.REGULAR_ATTRIBUTES)
                }
            },
            root
        )

        val checklistNode = CheckedTreeNode("Hello")
        checklistNode.add(CheckedTreeNode("Hello from inside"))

        (tree.model.root as CheckedTreeNode).add(checklistNode)
        TreeUtil.expand(tree, 1)

        val panel = panel {}
        panel.add(tree)

        return panel
    }

    private fun addMethodChecklist(nameMethod: String, checkBoxes: List<JBCheckBox>): Component {
        return createCheckListMethod(nameMethod, checkBoxes)
    }

    /**
     * Creates the base UI which has scroll panel and a button which adds checklist to the panel.
     */
    private fun createCheckList(): Component? {
        val panel = JBScrollPane()
        val content = panel {}
        content.layout = BoxLayout(content, BoxLayout.PAGE_AXIS)

        panel.setViewportView(content)

        val button = JButton("Add checklist")
        button.addActionListener {
            content.add(addMethodChecklist("Add Method", mutableListOf()))
        }
        content.add(button)

        return panel
    }

    /**
     * Skeleton code which returns the base scrollable panel on which we will add the other components.
     */
    private fun getCopyPasteTab(): Component? {

        val toolWindowPanel = SimpleToolWindowPanel(true)

        // Setting up the action group. Currently has default values which needs to be changed later.
        val actionManager = ActionManager.getInstance()
        val actionGroup = DefaultActionGroup("ACTION_GROUP", false)
        actionGroup.add(actionManager.getAction("deployAction"))
        val actionToolbar: ActionToolbar = actionManager.createActionToolbar("ACTION_TOOLBAR", actionGroup, true)
        toolWindowPanel.toolbar = actionToolbar.component

        val panel = JBScrollPane()
        testCaseUI = panel {}
        testCaseUI!!.layout = BoxLayout(testCaseUI, BoxLayout.PAGE_AXIS)
        panel.setViewportView(testCaseUI)

        toolWindowPanel.setContent(panel)
        return toolWindowPanel
    }

    // Constructor
    init {
        mainUI = JBTabbedPane(JBTabbedPane.TOP, JBTabbedPane.SCROLL_TAB_LAYOUT)

        // Function call which returns the tab for copy paste
        mainUI!!.addTab("CopyPaste", getCopyPasteTab())
        // Function call which returns the tab for checklist
        mainUI!!.addTab("Checklist", createCheckList())
    }
}
