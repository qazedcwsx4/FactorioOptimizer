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

    fun drawHooks(x: Double, y: Double, h: Double, amount: Int) {
        val ctx = GUI.getContext()
        val step = h / amount
        repeat(amount) {
            ctx.moveTo(x - 5, y + step / 2 + it * step)
            ctx.lineTo(x + 5, y + step / 2 + it * step)
        }
    }

    fun drawHook(pos: Pair<Double, Double>) {
        val ctx = GUI.getContext()
        ctx.moveTo(pos.first - 5, pos.second)
        ctx.lineTo(pos.first + 5, pos.second)
    }

    fun drawTable(x: Double, y: Double, w: Double, h: Double, barH: Double) {
        val ctx = GUI.getContext()
        ctx.rect(x, y, w, h)
        ctx.moveTo(x, y + barH / 2)
        ctx.lineTo(x + w, y + barH / 2)
        ctx.moveTo(x, y + barH)
        ctx.lineTo(x + w, y + barH)
    }
}