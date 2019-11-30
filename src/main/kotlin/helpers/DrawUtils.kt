package helpers

import org.w3c.dom.CanvasRenderingContext2D

object DrawUtils {
    fun drawText(ctx: CanvasRenderingContext2D, text: String, x: Double, y: Double, w: Double, h: Double) {
        val measured = ctx.measureText(text)
        val textHeight = measured.actualBoundingBoxAscent + measured.actualBoundingBoxDescent
        val textWidth = measured.width

        val offsetLeft = (w - textWidth) / 2
        val offsetUp = (h - textHeight) / 2

        ctx.fillText(text, x + offsetLeft, y + offsetUp + textHeight, w)
    }

    fun drawHooks(ctx: CanvasRenderingContext2D, x: Double, y: Double, h: Double, amount: Int) {
        val step = h / amount
        repeat(amount) {
            ctx.moveTo(x - 5, y + step / 2 + it * step)
            ctx.lineTo(x + 5, y + step / 2 + it * step)
        }
    }

    fun drawTable(ctx: CanvasRenderingContext2D, x: Double, y: Double, w: Double, h: Double, barH: Double) {
        ctx.fillStyle = "RGB(0,0,0)"
        ctx.rect(x, y, w, h)
        ctx.moveTo(x, y + barH)
        ctx.lineTo(x + w, y + barH)
    }
}