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

        DrawUtils.drawLine(
                Pair(pos.first - LENGTH, pos.second),
                Pair(pos.first + LENGTH, pos.second),
                chooseHookColour(getSingleAmount())
        )

        // draw connection
        connected.forEach {
            if (it.side == Side.LEFT) {
                val anotherPos = it.resolve()
                DrawUtils.drawTextNew(name, pos, anotherPos)

                DrawUtils.drawLine(
                        Pair(pos.first + LENGTH, pos.second),
                        Pair(anotherPos.first - LENGTH, anotherPos.second),
                        chooseBeltColour(getAmount())
                )
            }
        }
        if (connected.isEmpty()) {
            drawTextUnconnected(pos.first, pos.second)
        }

        getAmount()?.let {
            drawAmount(it, pos.first, pos.second)
        }
    }

    private fun chooseBeltColour(amount: Double?): String {
        if (amount == null) return "RGB(0,0,0)"
        return when (amount) {
            in 0.0..15.0 -> "RGB(222,138,41)"
            in 15.0..30.0 -> "RGB(0,64,255)"
            in 30.0..45.0 -> "RGB(255,8,0)"
            else -> "RGB(0,0,0)"
        }
    }

    private fun chooseHookColour(amount: Double?): String {
        if (amount == null) return "RGB(0,0,0)"
        return when (amount) {
            in 0.0..0.84 -> "RGB(222,138,41)"
            in 0.84..2.31 -> "RGB(115,195,108)"
            in 2.31..4.44 -> "RGB(0,64,255)"
            else -> "RGB(0,0,0)"
        }
    }

    private fun getAmount(): Double? {
        return getSingleAmount()?.times(parent.quantity)
    }

    private fun getSingleAmount(): Double? {
        val oneTimeAmount = if (side == Side.LEFT) {
            parent.selectedRecipe?.inputs?.get(this.number)
        } else {
            parent.selectedRecipe?.outputs?.get(this.number)
        }?.second ?: return null
        val energy = parent.selectedRecipe?.energy ?: return null
        return oneTimeAmount.toDouble() * parent.data.speed / energy
    }

    private fun drawAmount(amount: Double, x: Double, y: Double) {
        val margin = 7.0
        if (side == Side.LEFT) {
            DrawUtils.drawTextNew(
                    amount.toString(),
                    Pair(x + margin, y - 5.0),
                    Pair(x + (MACHINE_WIDTH / 2), y + 5.0),
                    DrawUtils.HAlign.LEFT,
                    DrawUtils.VAlign.TOP
            )
        } else {
            DrawUtils.drawTextNew(
                    amount.toString(),
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
            DrawUtils.drawTextNew(
                    name,
                    Pair(x - (MACHINE_WIDTH / 2), y - 5.0),
                    Pair(x - margin, y + 5.0),
                    DrawUtils.HAlign.RIGHT,
                    DrawUtils.VAlign.TOP
            )
        } else {
            DrawUtils.drawTextNew(
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