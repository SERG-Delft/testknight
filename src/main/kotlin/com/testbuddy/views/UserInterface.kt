package com.testbuddy.views

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.layout.panel
import java.awt.Component
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel

class UserInterface {

    private var mainUI: JBTabbedPane? = null
    private var testCaseUI: DialogPanel? = null

    /**
     * Returns the Component which will be displayed on the tool window
     */
    fun getContent(): JBTabbedPane? {
        return mainUI
    }

    fun createCheckBox(name: String, action: String): JBCheckBox {
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

    private fun createCheckListMethod(nameMethod: String, checkBoxes: List<JBCheckBox>): Component {
        checkBoxes.size

        val list: MutableList<JBCheckBox> = mutableListOf()
        list.add(createCheckBox("Item 1", "It works"))
        list.add(createCheckBox("Item 2", "It works2"))

        // var top : DefaultMutableTreeNode  = DefaultMutableTreeNode("The Java Series")
        // createNodes(top)
        // var tree: JBTreeTable = JBTreeTable()

//
//        var listItems : JBList <JBCheckBox> = JBList<JBCheckBox>(list)
//        listItems.setCellRenderer { list, value, index, isSelected, cellHasFocus -> value}
//        for (checkBox in list) {
//           listItems.add(checkBox)
//        }

//         list2.cellRenderer

        return panel {

            hideableRow(nameMethod) {
                right {
                    for (checkBox in list) {
                        row {
                            checkBox()
                        }
                    }
                }
            }
        }
    }

    private fun addMethodChecklist(nameMethod: String, checkBoxes: List<JBCheckBox>): Component {
        return createCheckListMethod(nameMethod, checkBoxes)
    }

    private fun createCheckList(): Component? {
        // var scrollPanel = JBScrollPane()
        // var panel= JPanel()
        val panel = JBScrollPane()
        val content = JPanel()
        content.layout = BoxLayout(content, BoxLayout.PAGE_AXIS)
        panel.setViewportView(content)

        val button = JButton("test")
        button.addActionListener {
            content.add(addMethodChecklist("Add Method", mutableListOf()))
        }
        content.add(button)

//        content.add(addMethodChecklist("ajwijw", mutableListOf()))
//        content.add(addMethodChecklist("qko2kqo", mutableListOf()))
//        checkListMethods.add(addMethodChecklist("ajwijw", mutableListOf()))
//        return panel() {
//            for (method in checkListMethods)
//                panel(){
//                    method
//                }
//        }

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
