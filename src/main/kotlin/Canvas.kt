import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window

class Canvas {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    val width = window.innerWidth
    val height = window.innerHeight

    init {
        val scale = window.devicePixelRatio
        canvas.width = (window.innerWidth * scale).toInt()
        canvas.height = (window.innerHeight * scale).toInt()
        canvas.style.width = "${window.innerWidth}px"
        canvas.style.height = "${window.innerHeight}px"
        document.body!!.appendChild(canvas)

        context.scale(scale, scale)
        context.fillStyle = "RGB(0,0,0)"
        context.font = "12px sans-serif"
        context.fillStyle = "RGB(0,0,0)"
        context.lineWidth = 2.0
        context.clearRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())
    }

    fun drawRect() {
        context.fillStyle = "black"
        context.strokeRect(10.0, 10.0, 110.0, 110.0)
    }
}