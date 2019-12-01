import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import kotlin.browser.window

class Chart(
        private val factory: MachineFactory
) {
    private val machines = mutableListOf<Machine>()

    init {
        GUI.addListener("mousedown", ::handleMouseDown)
    }

    private fun checkHeaders(x: Double, y: Double): Machine? {
        return machines.firstOrNull { y in it.y..it.y + BAR_HEIGHT && x in it.x..it.x + WIDTH }
    }

    private fun checkBodies(x: Double, y: Double): Machine? {
        return machines.firstOrNull { y in it.y + BAR_HEIGHT..it.y + HEIGHT && x in it.x..it.x + WIDTH }
    }

    private fun handleMouseDown(evt: Event) {
        evt as MouseEvent
        val x = evt.offsetX
        val y = evt.offsetY

        checkHeaders(x, y)?.let { dragMachine(x, y, it); return }
        createMachine(x, y)
        drawState()
    }

    private fun createMachine(x: Double, y: Double) {
        machines.add(factory.createMachine(GUI.getCurrentMachine(), x, y))
    }

    private fun dragMachine(x: Double, y: Double, machine: Machine) {
        val diffX = x - machine.x
        val diffY = y - machine.y

        val handleDrag = fun(evt: Event) {
            evt as MouseEvent
            machine.x = evt.offsetX - diffX
            machine.y = evt.offsetY - diffY
            window.requestAnimationFrame { drawState() }
        }

        lateinit var handleMouseUp: (Event) -> Unit
        handleMouseUp = {
            println("deletuje")
            it as MouseEvent
            GUI.removeListener("mousemove", handleDrag)
            GUI.removeListener("mouseup", handleMouseUp)
        }
        GUI.addListener("mousemove", handleDrag)
        GUI.addListener("mouseup", handleMouseUp)
    }

    private fun drawState() {
        GUI.clearScreen()
        machines.forEach {
            it.draw()
        }
    }
}