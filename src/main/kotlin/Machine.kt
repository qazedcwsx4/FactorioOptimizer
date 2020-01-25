import helpers.DrawUtils
import helpers.DrawUtils.drawHook
import recipe.MachineData
import recipe.Recipe

const val WIDTH = 150.0
const val HEIGHT = 120.0
const val BAR_HEIGHT = 40.0
const val HOOK_OFFSET = 10.0

enum class Side {
    LEFT,
    RIGHT
}

data class Machine(
        val data: MachineData,
        val inputsCount: Int,
        val outputsCount: Int,
        var x: Double = 0.0,
        var y: Double = 0.0,
        var selectedRecipe: Recipe? = null
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
        DrawUtils.drawTable(x, y, WIDTH, HEIGHT, BAR_HEIGHT)
        DrawUtils.drawText(data.name, x, y, WIDTH, BAR_HEIGHT / 2)
        selectedRecipe?.let { DrawUtils.drawText(it.name, x, y + BAR_HEIGHT / 2, WIDTH, BAR_HEIGHT / 2) }

        drawHooks()
        ctx.stroke()
        ctx.closePath()
    }

    private fun drawHooks() {
        repeat(inputsCount) {
            drawHook(getHook(it, inputsCount, Side.LEFT))
        }
        repeat(outputsCount) {
            drawHook(getHook(it, outputsCount, Side.RIGHT))
        }
    }

    private fun getHook(num: Int, outOf: Int, side: Side): Pair<Double, Double> {
        return when (side) {
            Side.LEFT -> Pair(x, y + BAR_HEIGHT + HOOK_OFFSET + (HEIGHT - 2 * HOOK_OFFSET - BAR_HEIGHT) * num / (outOf - 1))
            Side.RIGHT -> Pair(x + WIDTH, y + BAR_HEIGHT + HOOK_OFFSET + (HEIGHT - 2 * HOOK_OFFSET - BAR_HEIGHT) * num / (outOf - 1))
        }
    }
}