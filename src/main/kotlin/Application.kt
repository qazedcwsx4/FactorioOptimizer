import recipe.RecipeRegistry

class Application(
        config: String
) {
    init {
        val registry = RecipeRegistry(config)
        val factory = MachineFactory(registry)
        val chart = Chart(factory)
        GUI.updateMachines(registry.getMachineNames())
    }
}