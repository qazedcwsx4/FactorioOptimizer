import draw.Machine
import recipe.RecipeRegistry

class MachineFactory(
        val registry: RecipeRegistry
) {
    fun createMachine(machine: String, x: Double, y: Double): Machine {
        return Machine(registry.getMachine(machine), x, y)
    }
}