import helpers.DrawUtils
import recipe.MachineData
import recipe.Recipe

const val MACHINE_WIDTH = 200.0
const val MACHINE_HEIGHT = 120.0
const val BAR_HEIGHT = 40.0
const val HOOK_OFFSET = 10.0

enum class Side {
    LEFT,
    RIGHT
}

data class Machine(
        val data: MachineData,
        var x: Double = 0.0,
        var y: Double = 0.0,
        var inputs: List<Hook> = listOf(),
        var outputs: List<Hook> = listOf(),
        var selectedRecipe: Recipe? = null
) {
    init {
        inputs = listOf(Hook("", Side.LEFT, 0, this))
        outputs = listOf(Hook("", Side.RIGHT, 0, this))
    }

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
        drawTable(x, y, MACHINE_WIDTH, MACHINE_HEIGHT, BAR_HEIGHT)
        DrawUtils.drawText(data.name, x, y, MACHINE_WIDTH, BAR_HEIGHT / 2)
        selectedRecipe?.let { DrawUtils.drawText(it.name, x, y + BAR_HEIGHT / 2, MACHINE_WIDTH, BAR_HEIGHT / 2) }

        drawHooks()
        ctx.stroke()
        ctx.closePath()
    }

    fun changeRecipe(selectedOption: Recipe?){
        (inputs + outputs).forEach {
            it.unhook()
        }
        selectedRecipe = selectedOption
        if (selectedRecipe == null){
            inputs = listOf(Hook("", Side.LEFT, 0, this))
            outputs = listOf(Hook("", Side.RIGHT, 0, this))
        } else {
            var count = -1
            inputs = selectedRecipe!!.inputs.map { count++; Hook(it.first, Side.LEFT, count, this)}
            count = -1
            outputs = selectedRecipe!!.outputs.map { count++; Hook(it.first, Side.RIGHT, count, this)}
        }
    }

    private fun drawHooks() {
        (inputs + outputs).forEach {
            it.draw()
        }
    }

    private fun drawTable(x: Double, y: Double, w: Double, h: Double, barH: Double) {
        val ctx = GUI.getContext()
        ctx.rect(x, y, w, h)
        ctx.moveTo(x, y + barH / 2)
        ctx.lineTo(x + w, y + barH / 2)
        ctx.moveTo(x, y + barH)
        ctx.lineTo(x + w, y + barH)
    }

}