import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent

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

    private fun handleMouseDown(evt: Event) {
        evt as MouseEvent
        val x = evt.offsetX
        val y = evt.offsetY
        checkHeaders(x, y)?.let { handleDrag(x, y, it); return }
        handleCreate(x, y)
        drawState()
    }

    private fun handleCreate(x: Double, y: Double) {
        machines.add(factory.createMachine(GUI.getCurrentMachine(), x, y))
    }

    private fun handleDrag(x: Double, y: Double, machine: Machine) {
        println("XD")
    }

    private fun drawState() {
        GUI.clearScreen()
        machines.forEach {
            it.draw()
        }
    }
}