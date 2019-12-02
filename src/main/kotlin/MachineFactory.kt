class MachineFactory(
        val registry: RecipeRegistry
) {
    fun createMachine(machine: String, x: Double, y: Double): Machine {
        return registry.prototypes[machine]?.copy(x = x, y = y)
                ?: throw IllegalArgumentException("machine $machine doesn't exist")
    }
}