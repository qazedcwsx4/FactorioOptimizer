import helpers.DrawUtils
import recipe.MachineData

const val WIDTH = 150.0
const val HEIGHT = 100.0
const val BAR_HEIGHT = 20.0

data class Machine(
        val data: MachineData,
        val inputsCount: Int,
        val outputsCount: Int,
        var x: Double = 0.0,
        var y: Double = 0.0
) {
    fun draw(selected: Boolean) {
        val ctx = GUI.getContext()

        if (selected) {
            ctx.fillStyle = "RGB(70,124,232)"
            ctx.strokeStyle = "RGB(70,124,232)"
        } else {
            ctx.fillStyle = "RGB(0,0,0)"
            ctx.strokeStyle = "RGB(0,0,0)"
        }
        ctx.beginPath()
        DrawUtils.drawTable(ctx, x, y, WIDTH, HEIGHT, BAR_HEIGHT)
        DrawUtils.drawText(ctx, data.name, x, y, WIDTH, BAR_HEIGHT)
        DrawUtils.drawHooks(ctx, x, y + BAR_HEIGHT, HEIGHT - BAR_HEIGHT, inputsCount)
        DrawUtils.drawHooks(ctx, x + WIDTH, y + BAR_HEIGHT, HEIGHT - BAR_HEIGHT, inputsCount)
        ctx.stroke()
        ctx.closePath()
    }
}