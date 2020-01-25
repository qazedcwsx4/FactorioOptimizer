import recipe.RecipeRegistry

class MachineFactory(
        val registry: RecipeRegistry
) {
    fun createMachine(machine: String, x: Double, y: Double): Machine {
        return Machine(registry.getMachine(machine), 2, 2, x, y)
    }
}