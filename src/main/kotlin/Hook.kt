import helpers.DrawUtils

const val LENGTH = 5

data class Hook(
        val name: String,
        val side: Side,
        val number: Int,
        val parent: Machine,
        var connected: MutableList<Hook> = mutableListOf()
) {
    fun draw() {
        val ctx = GUI.getContext()
        val pos = resolve()
        ctx.moveTo(pos.first - LENGTH, pos.second)
        ctx.lineTo(pos.first + LENGTH, pos.second)


        // draw connection
        connected.forEach {
            if (it.side == Side.LEFT) {
                val anotherPos = it.resolve()
                DrawUtils.drawCentered(name, pos, anotherPos)
                ctx.moveTo(pos.first + LENGTH, pos.second)
                ctx.lineTo(anotherPos.first - LENGTH, anotherPos.second)
            }
        }
        if (connected.isEmpty()) {
            drawTextUnconnected(pos.first, pos.second)
        }

        if (parent.selectedRecipe != null){
            drawAmount(pos.first, pos.second)
        }
    }

    private fun getAmount(): Double {
        val oneTimeAmount = if (side == Side.LEFT) {
            parent.selectedRecipe?.inputs?.get(this.number)
        } else {
            parent.selectedRecipe?.outputs?.get(this.number)
        }?.second ?: throw Exception("Recipe is not selected for ${parent.data.name}")
        val energy = parent.selectedRecipe?.energy ?: throw Exception("Recipe is not selected for ${parent.data.name}")
        return oneTimeAmount.toDouble() * parent.quantity * parent.data.speed * energy
    }

    private fun drawAmount(x: Double, y: Double) {
        val margin = 7.0
        if (side == Side.LEFT) {
            DrawUtils.drawCentered(
                    getAmount().toString(),
                    Pair(x + margin, y - 5.0),
                    Pair(x + (MACHINE_WIDTH / 2), y + 5.0),
                    DrawUtils.HAlign.LEFT,
                    DrawUtils.VAlign.TOP
            )
        } else {
            DrawUtils.drawCentered(
                    getAmount().toString(),
                    Pair(x - (MACHINE_WIDTH / 2), y - 5.0),
                    Pair(x - margin, y + 5.0),
                    DrawUtils.HAlign.RIGHT,
                    DrawUtils.VAlign.TOP
            )
        }
    }

    private fun drawTextUnconnected(x: Double, y: Double) {
        val margin = 7.0
        if (side == Side.LEFT) {
            DrawUtils.drawCentered(
                    name,
                    Pair(x - (MACHINE_WIDTH / 2), y - 5.0),
                    Pair(x - margin, y + 5.0),
                    DrawUtils.HAlign.RIGHT,
                    DrawUtils.VAlign.TOP
            )
        } else {
            DrawUtils.drawCentered(
                    name,
                    Pair(x + margin, y - 5.0),
                    Pair(x + (MACHINE_WIDTH / 2), y + 5.0),
                    DrawUtils.HAlign.LEFT,
                    DrawUtils.VAlign.TOP
            )
        }
    }

    fun resolve(): Pair<Double, Double> {
        val outOf = when (side) {
            Side.LEFT -> parent.inputs.count() - 1
            Side.RIGHT -> parent.outputs.count() - 1
        }

        val spaceForHooks = MACHINE_HEIGHT - 2 * HOOK_OFFSET - BAR_HEIGHT
        val y = parent.y + BAR_HEIGHT + HOOK_OFFSET
        val x = when (side) {
            Side.LEFT -> parent.x
            Side.RIGHT -> parent.x + MACHINE_WIDTH
        }

        // handle 1 hook case
        if (outOf == 0) return Pair(x, y + spaceForHooks / 2)

        return Pair(x, y + spaceForHooks * number / outOf)
    }

    fun hook(o: Hook) {
        if (o == this) {
            unhookAll()
        } else if (canBeHooked(o)) {
            connected.add(o)
            o.connected.add(this)
        }
    }

    fun unhookAll() {
        connected.forEach {
            it.unhook(this)
        }
        connected = mutableListOf()
    }

    fun unhook(o: Hook) {
        connected.remove(o)
    }

    fun canBeHooked(o: Hook): Boolean {
        return (o.side != side && (o.name == "" || name == "" || o.name == this.name))
    }
}