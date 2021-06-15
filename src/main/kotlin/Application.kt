import draw.Chart
import draw.GUI
import recipe.RecipeRegistry

class Application(
        config: String
) {
    init {
        val registry = RecipeRegistry(config)
        val factory = MachineFactory(registry)
        val chart = Chart(factory, "New chart")
        GUI.updateMachineList(registry.getMachineNames())
    }
}