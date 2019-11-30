import org.w3c.dom.*
import org.w3c.dom.events.Event
import kotlin.browser.document

object GUI {
    private val machinePicker = document.createElement("select") as HTMLSelectElement
    private val canvas: Canvas

    init {
        document.body!!.appendChild(machinePicker)
        canvas = Canvas()
    }

    fun update(machines: List<String>) {
        machines.forEach {
            machinePicker.add(Option(it))
        }
    }

    fun clearScreen() {
        canvas.context.clearRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())
    }

    fun getCurrentMachine(): String {
        return (machinePicker.selectedOptions[0] as Option).text
    }

    fun getContext(): CanvasRenderingContext2D {
        return canvas.context
    }

    fun addListener(type: String, listener: (Event) -> Unit) {
        canvas.canvas.addEventListener(type, listener)
    }

    fun removeListener(type: String, listener: (Event) -> Unit) {
        canvas.canvas.removeEventListener(type, listener)
    }
}