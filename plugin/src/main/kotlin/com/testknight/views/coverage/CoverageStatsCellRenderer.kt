package com.testknight.views.coverage

import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.JBColor
import com.intellij.ui.SimpleTextAttributes
import com.testknight.models.CoverageStatsObject
import javax.swing.JTable

class CoverageStatsCellRenderer : ColoredTableCellRenderer() {
    override fun customizeCellRenderer(
        table: JTable,
        value: Any?,
        selected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ) {

        if (value is CoverageStatsObject) {

            append("${value.percentageCovered}%")
            val percentChange = value.percentChange
            if (percentChange > 0) {
                append(
                    " (+$percentChange)%",
                    SimpleTextAttributes(
                        SimpleTextAttributes.STYLE_SMALLER,
                        JBColor.GREEN
                    )
                )
            } else if (percentChange < 0) {
                append(
                    " ($percentChange)%",
                    SimpleTextAttributes(
                        SimpleTextAttributes.STYLE_SMALLER,
                        JBColor.RED
                    )
                )
            } else {
                append(" (+0)%", SimpleTextAttributes.GRAY_SMALL_ATTRIBUTES)
            }

            append(" (${value.coveredLines}/${value.allLines})")
        }
    }
}
