const val LENGTH = 5

data class Hook(
        val name: String,
        val side: Side,
        val number: Int,
        val parent: Machine,
        var connected: Hook? = null
) {
    fun draw() {
        val ctx = GUI.getContext()
        val pos = resolve()
        ctx.moveTo(pos.first - LENGTH, pos.second)
        ctx.lineTo(pos.first + LENGTH, pos.second)

        // draw connection
        connected?.let {
            if (connected!!.side == Side.LEFT) {
                val anotherPos = it.resolve()
                ctx.moveTo(pos.first + LENGTH, pos.second)
                ctx.lineTo(anotherPos.first - LENGTH, anotherPos.second)
            }
        }
    }

    fun resolve(): Pair<Double, Double> {
        val outOf = when (side) {
            Side.LEFT -> parent.inputs.count() - 1
            Side.RIGHT -> parent.outputs.count() - 1
        }

        val spaceForHooks = HEIGHT - 2 * HOOK_OFFSET - BAR_HEIGHT
        val y = parent.y + BAR_HEIGHT + HOOK_OFFSET
        val x = when (side) {
            Side.LEFT -> parent.x
            Side.RIGHT -> parent.x + WIDTH
        }

        // handle 1 hook case
        if (outOf == 0) return Pair(x, y + spaceForHooks / 2)

        return Pair(x, y + spaceForHooks * number / outOf)
    }

    fun hook(o: Hook) {
        if (o == this) {
            unhook()
        } else if (canBeHooked(o)) {
            connected = o
            o.connected = this
        }
    }

    fun unhook() {
        connected?.connected = null
        connected = null
    }

    fun canBeHooked(o: Hook): Boolean {
        return (o.name == "" || o.name == this.name)
    }
}