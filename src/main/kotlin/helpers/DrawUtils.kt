package helpers

import org.w3c.dom.CanvasRenderingContext2D

object DrawUtils {
    fun drawText(text: String, x: Double, y: Double, w: Double, h: Double) {
        val ctx = GUI.getContext()
        val measured = ctx.measureText(text)
        val textHeight = measured.actualBoundingBoxAscent + measured.actualBoundingBoxDescent
        val textWidth = measured.width

        val offsetLeft = (w - textWidth) / 2
        val offsetUp = (h - textHeight) / 2

        ctx.fillText(text, x + offsetLeft, y + offsetUp + textHeight, w)
    }

    fun drawText2() {

    }
}