import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import kotlin.browser.window

class Chart(
        private val factory: MachineFactory
) {
    private val machines = mutableListOf<Machine>()
    private var selected: Machine? = null

    init {
        GUI.addListener("mousedown", ::handleMouseDown)
        GUI.setTargetedChart(this)
        GUI.addRecipesListener("change", ::handleRecipeChange)
    }

    private fun handleRecipeChange(evt: Event) {
        selected?.selectedRecipe = factory.registry.getRecipe((evt.target as HTMLSelectElement).value)
        drawState()
    }

    private fun checkHeaders(x: Double, y: Double): Machine? {
        return machines.firstOrNull { y in it.y..it.y + BAR_HEIGHT && x in it.x..it.x + WIDTH }
    }

    private fun checkBodies(x: Double, y: Double): Machine? {
        return machines.firstOrNull { y in it.y + BAR_HEIGHT..it.y + HEIGHT && x in it.x..it.x + WIDTH }
    }

    private fun checkHooks(x: Double, y: Double): Hook? {
        val boxing = 10
        val concerned = machines.filter {
            y in it.y - boxing..it.y + HEIGHT + BAR_HEIGHT + boxing
                    && x in it.x - boxing..it.x + WIDTH + boxing
        }.flatMap { it.inputs + it.outputs }
        return concerned.firstOrNull {
            val pos = it.resolve();
            y in pos.second - 10..pos.second + 10 &&
                    x in pos.first - 10..pos.first + 10
        }
    }

    private fun handleMouseDown(evt: Event) {
        evt as MouseEvent
        val x = evt.offsetX
        val y = evt.offsetY

        selected = null
        checkHeaders(x, y)?.let { dragMachine(x, y, it); return }
        checkHooks(x, y)?.let { dragHook(x, y, it); return }
        checkBodies(x, y)?.let { selectMachine(it); return }
        createMachine(x, y)
    }

    private fun createMachine(x: Double, y: Double) {
        machines.add(factory.createMachine(GUI.getCurrentMachine(), x, y))
        drawState()
    }

    private fun dragHook(x: Double, y: Double, hook: Hook) {
        var newX = x
        var newY = y
        val handleDrag = fun(evt: Event) {
            evt as MouseEvent
            newX = evt.offsetX
            newY = evt.offsetY
        }

        lateinit var handleMouseUp: (Event) -> Unit
        handleMouseUp = {
            it as MouseEvent
            GUI.removeListener("mousemove", handleDrag)
            GUI.removeListener("mouseup", handleMouseUp)

            checkHooks(newX, newY)?.let {
                hook.hook(it)
                drawState()
            }
        }
        GUI.addListener("mousemove", handleDrag)
        GUI.addListener("mouseup", handleMouseUp)
    }

    private fun dragMachine(x: Double, y: Double, machine: Machine) {
        val diffX = x - machine.x
        val diffY = y - machine.y

        selectMachine(machine)

        val handleDrag = fun(evt: Event) {
            evt as MouseEvent
            machine.x = evt.offsetX - diffX
            machine.y = evt.offsetY - diffY
            window.requestAnimationFrame { drawState() }
        }

        lateinit var handleMouseUp: (Event) -> Unit
        handleMouseUp = {
            it as MouseEvent
            GUI.removeListener("mousemove", handleDrag)
            GUI.removeListener("mouseup", handleMouseUp)
        }
        GUI.addListener("mousemove", handleDrag)
        GUI.addListener("mouseup", handleMouseUp)
    }

    private fun selectMachine(machine: Machine) {
        selected = machine
        GUI.updateRecipes(factory.registry.getRecipes(machine.data.name))

        if (selected?.selectedRecipe == null) GUI.selectRecipe(0)
        else GUI.selectRecipe(factory.registry.getRecipeIndex(selected?.selectedRecipe!!.name))

        drawState()
    }

    private fun drawState() {
        GUI.clearScreen()
        machines.forEach {
            it.draw(it == selected)
        }
    }
}