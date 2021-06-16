package com.testknight.extensions

import com.intellij.coverage.LineMarkerRendererWithErrorStripe
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.markup.LineMarkerRendererEx.Position
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle

class DiffCoverageLineMarkerRenderer(val color: Color) : LineMarkerRendererWithErrorStripe {

    override fun paint(editor: Editor, g: Graphics, r: Rectangle) {
        g.color = color
        g.fillRect(r.x, r.y, r.width, +r.height)
    }

    override fun getPosition(): Position {
        return Position.LEFT
    }

    override fun getErrorStripeColor(editor: Editor?): Color {
        return Color.CYAN
    }
}
