package draw

import MachineFactory
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import kotlin.browser.document
import kotlin.browser.window

class Chart(
    private val factory: MachineFactory,
    private val gui: GUI,
    private var name: String
) {
    private val machines = mutableListOf<Machine>()
    var selected: Machine? = null

    init {
        document.addEventListener("keydown", ::handleKeyDown)
        gui.addListener("mousedown", ::handleMouseDown)
        gui.addRecipesListener("change", ::handleRecipeChange)
        gui.addQuantityListener("input", ::handleQuantityChange)
    }

    private fun handleQuantityChange(evt: Event) {
        val quantity = (evt.target as HTMLInputElement).value.toInt()
        selected?.quantity = quantity
        drawState()
    }

    private fun handleRecipeChange(evt: Event) {
        val selectedOption = factory.registry.getRecipe((evt.target as HTMLSelectElement).value)
        selected?.changeRecipe(selectedOption)
        drawState()
    }

    private fun checkHeaders(x: Double, y: Double): Machine? {
        return machines.firstOrNull { y in it.y..it.y + BAR_HEIGHT && x in it.x..it.x + MACHINE_WIDTH }
    }

    private fun checkBodies(x: Double, y: Double): Machine? {
        return machines.firstOrNull { y in it.y + BAR_HEIGHT..it.y + MACHINE_HEIGHT && x in it.x..it.x + MACHINE_WIDTH }
    }

    private fun checkHooks(x: Double, y: Double): Hook? {
        val boxing = 10
        val concerned = machines.filter {
            y in it.y - boxing..it.y + MACHINE_HEIGHT + BAR_HEIGHT + boxing
                    && x in it.x - boxing..it.x + MACHINE_WIDTH + boxing
        }.flatMap { it.inputs + it.outputs }
        return concerned.firstOrNull {
            val pos = it.resolve()
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

    private fun handleKeyDown(evt: Event) {
        evt as KeyboardEvent

        // handle delete
        if (evt.keyCode == 46) {
            selected?.let {
                it.prepareToRemove()
                machines.remove(it)
                selected = null
            }
        }
        drawState()
    }

    private fun createMachine(x: Double, y: Double) {
        machines.add(factory.createMachine(gui.getCurrentMachine(), x, y))
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
            gui.removeListener("mousemove", handleDrag)
            gui.removeListener("mouseup", handleMouseUp)

            checkHooks(newX, newY)?.let {
                hook.hook(it)
                drawState()
            }
        }
        gui.addListener("mousemove", handleDrag)
        gui.addListener("mouseup", handleMouseUp)
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
            gui.removeListener("mousemove", handleDrag)
            gui.removeListener("mouseup", handleMouseUp)
        }
        gui.addListener("mousemove", handleDrag)
        gui.addListener("mouseup", handleMouseUp)
    }

    private fun selectMachine(machine: Machine) {
        selected = machine
        gui.updateRecipeList(factory.registry.getRecipes(machine.data.name))
        gui.updateQuantity(machine.quantity)
        if (selected?.selectedRecipe == null) gui.selectRecipe(0)
        else gui.selectRecipe(factory.registry.getRecipeIndex(selected?.selectedRecipe!!.name))

        drawState()
    }

    private fun drawState() {
        gui.clearScreen()
        machines.forEach {
            it.draw(it == selected)
        }
    }
}