import kotlin.browser.window
import kotlin.js.Promise

class Application(
        config: String
) {
    init {
        val registry = RecipeRegistry(config)
        val factory = MachineFactory(registry)
        val chart = Chart(factory)
        GUI.update(registry.getMachines())
    }
}