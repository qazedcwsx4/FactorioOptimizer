class MachineFactory(
        private val recipeRegistry: RecipeRegistry
) {
    fun createMachine(machine: String, x: Double, y: Double): Machine {
        return recipeRegistry.prototypes[machine]?.copy(x = x, y = y)
                ?: throw IllegalArgumentException("machine $machine doesn't exist")
    }
}