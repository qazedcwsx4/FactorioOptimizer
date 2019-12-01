import helpers.DrawUtils

const val WIDTH = 150.0
const val HEIGHT = 100.0
const val BAR_HEIGHT = 20.0

data class Machine(
        val type: String,
        val inputsCount: Int,
        val outputsCount: Int,
        var x: Double = 0.0,
        var y: Double = 0.0
) {
    fun draw() {
        val ctx = GUI.getContext()
        ctx.beginPath()

        DrawUtils.drawTable(ctx, x, y, WIDTH, HEIGHT, BAR_HEIGHT)
        DrawUtils.drawText(ctx, type, x, y, WIDTH, BAR_HEIGHT)
        DrawUtils.drawHooks(ctx, x, y + BAR_HEIGHT, HEIGHT - BAR_HEIGHT, inputsCount)
        DrawUtils.drawHooks(ctx, x + WIDTH, y + BAR_HEIGHT, HEIGHT - BAR_HEIGHT, inputsCount)
        ctx.stroke()
        ctx.closePath()
    }
}