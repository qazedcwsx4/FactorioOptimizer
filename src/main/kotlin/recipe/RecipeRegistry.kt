package recipe

import org.w3c.xhr.XMLHttpRequest

open class RecipeRegistry(path: String) {
    private val recipes: List<Recipe> = ConfigParser.parseRecipes(path + "recipe.json")
    private val machines: List<MachineData> = ConfigParser.parseMachines(path + "assembling-machine.json")

    init {
        println(recipes)
    }

    private fun openFile(path: String): String {
        val xhr = XMLHttpRequest()
        xhr.open("GET", path, false)
        xhr.send()
        return xhr.responseText
    }

    fun getMachineNames(): List<String> {
        return machines.map { it.name }
    }

    fun getMachine(machine: String): MachineData {
        return machines.find { it.name == machine }
                ?: throw IllegalArgumentException("machine $machine doesn't exist")
    }

    fun getRecipes(machine: String): List<Recipe> {
        val machineData = getMachine(machine)
        return machineData.categories.flatMap { category ->
            recipes.filter { it.category == category }
        }
    }

    fun getRecipe(name: String): Recipe {
        return recipes.find { it.name == name } ?: throw IllegalArgumentException("recipe $name doesn't exist")
    }

    fun getRecipeIndex(name: String): Int {
        return recipes.indexOf(recipes.find { it.name == name }
                ?: throw IllegalArgumentException("recipe $name doesn't exist")) + 1
    }
}
