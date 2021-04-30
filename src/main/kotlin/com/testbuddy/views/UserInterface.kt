package com.testbuddy.views

import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.layout.panel
import java.awt.Component
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel

class UserInterface() {

    private var mainUI: JBTabbedPane? = null

    fun getContent(): JBTabbedPane? {
        return mainUI
    }

    fun createCheckBox(name: String, action: String): JBCheckBox {
        var x = JBCheckBox(name)
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

        var list: MutableList<JBCheckBox> = mutableListOf()
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
        var panel = JBScrollPane()
        var content = JPanel()
        content.setLayout(BoxLayout(content, BoxLayout.PAGE_AXIS))
        panel.setViewportView(content)

        var button = JButton("test")
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

    // Constructor
    init {
        mainUI = JBTabbedPane()
        // Function call which returns the tab for copy paste
        mainUI!!.addTab("Checklist", createCheckList())

        // Function call which returns the tab for checklist
    }
}
