package com.testbuddy.views

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
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

    fun createCheckBox(name: String, action: String): JBCheckBox {
        val x = JBCheckBox(name)
        x.addActionListener { e ->
            run {
                if (x.isSelected) {
                    println(action)
                }
            }
        }
        return x
    }

    fun createCheckListMethod(nameMethod: String, checkBoxes: List<JBCheckBox>): Component {
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
        // eturn listItems\\
    }

    fun addMethodChecklist(nameMethod: String, checkBoxes: List<JBCheckBox>): Component {
        return createCheckListMethod(nameMethod, checkBoxes)
    }

    fun createCheckList(): Component? {
        // var scrollPanel = JBScrollPane()
        // var panel= JPanel()
        val panel = JBScrollPane()
        val content = JPanel()
        content.setLayout(BoxLayout(content, BoxLayout.PAGE_AXIS))
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
    @Suppress("MagicNumber")
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
        mainUI = JBTabbedPane(JBTabbedPane.TOP, JBTabbedPane.SCROLL_TAB_LAYOUT)
        mainUI!!.addTab("CopyPaste", getCopyPasteTab())

        // Function call which returns the tab for copy paste
        mainUI!!.addTab("Checklist", createCheckList())

        // Function call which returns the tab for checklist

        // This button is just to give an idea how adding new test cases would look like
        // This will be removed soon after communication with backend has been established.
        addTest = JButton("Add new test")
        addTest!!.addActionListener { updateUI() }
    }
}
