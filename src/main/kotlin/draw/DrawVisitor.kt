package draw

import helpers.DrawUtils

class DrawVisitor(
    private val gui: GUI
) {
    //region machine

    fun visit(machine: Machine) {
        val ctx = gui.getContext()

        if (gui.chart.selected == machine) {
            ctx.fillStyle = "RGB(70,124,232)"
            ctx.strokeStyle = "RGB(70,124,232)"
        } else {
            ctx.fillStyle = "RGB(0,0,0)"
            ctx.strokeStyle = "RGB(0,0,0)"
        }
        ctx.beginPath()
        drawTable(machine.x, machine.y, MACHINE_WIDTH, MACHINE_HEIGHT, BAR_HEIGHT)
        DrawUtils.drawText(
            machine.data.name + " " + machine.quantity.toString(),
            machine.x,
            machine.y,
            MACHINE_WIDTH,
            BAR_HEIGHT / 2
        )
        machine.selectedRecipe?.let {
            DrawUtils.drawText(
                it.name,
                machine.x,
                machine.y + BAR_HEIGHT / 2,
                MACHINE_WIDTH,
                BAR_HEIGHT / 2
            )
        }
        ctx.stroke()
        ctx.closePath()

        drawHooks(machine)
    }

    private fun drawTable(x: Double, y: Double, w: Double, h: Double, barH: Double) {
        val ctx = gui.getContext()
        ctx.rect(x, y, w, h)
        ctx.moveTo(x, y + barH / 2)
        ctx.lineTo(x + w, y + barH / 2)
        ctx.moveTo(x, y + barH)
        ctx.lineTo(x + w, y + barH)
    }


    private fun drawHooks(machine: Machine) {
        (machine.inputs + machine.outputs).forEach {
            it.draw()
        }
    }
    //endregion

    fun visit(o: Hook) {

    }


}