package helpers

import kotlin.math.max
import kotlin.math.min

object DrawUtils {
    enum class HAlign {
        LEFT,
        CENTER,
        RIGHT
    }

    enum class VAlign {
        TOP,
        CENTER,
        BOTTOM
    }

    fun drawText(text: String, x: Double, y: Double, w: Double, h: Double) {
        val ctx = GUI.getContext()
        val measured = ctx.measureText(text)
        val textHeight = measured.actualBoundingBoxAscent + measured.actualBoundingBoxDescent
        val textWidth = measured.width

        val offsetLeft = (w - textWidth) / 2
        val offsetUp = (h - textHeight) / 2

        ctx.fillText(text, x + offsetLeft, y + offsetUp + textHeight, w)
    }

    fun drawCentered(
            text: String,
            pos1: Pair<Double, Double>,
            pos2: Pair<Double, Double>,
            hAlign: HAlign = HAlign.CENTER,
            vAlign: VAlign = VAlign.CENTER
    ) {
        val upperLeft = Pair(min(pos1.first, pos2.first), min(pos1.second, pos2.second))
        val lowerRight = Pair(max(pos1.first, pos2.first), max(pos1.second, pos2.second))

        val ctx = GUI.getContext()
        val measured = ctx.measureText(text)
        val textHeight = measured.actualBoundingBoxAscent + measured.actualBoundingBoxDescent
        val textWidth = measured.width

        val boxWidth = lowerRight.first - upperLeft.first
        val boxHeight = lowerRight.second - upperLeft.second

        val offsetLeft = when (hAlign) {
            HAlign.LEFT -> 0.0
            HAlign.CENTER -> (boxWidth - textWidth) / 2
            HAlign.RIGHT -> boxWidth - textWidth
        }

        val offsetTop = when (vAlign) {
            VAlign.TOP -> 0.0
            VAlign.CENTER -> (boxHeight - textHeight) / 2
            VAlign.BOTTOM -> boxHeight - textHeight
        }

        ctx.fillText(text, upperLeft.first + offsetLeft, upperLeft.second + offsetTop + textHeight, boxWidth)
    }
}