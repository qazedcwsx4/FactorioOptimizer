import org.w3c.xhr.XMLHttpRequest
import kotlin.math.max

open class RecipeRegistry(path: String) {
    private val recipes: Map<String, List<Recipe>>
    val prototypes: Map<String, Machine>

    init {
        recipes = parseData(openFile(path))
        prototypes = createPrototypes(recipes)
        println(recipes)
    }

    private fun openFile(path: String): String {
        val xhr = XMLHttpRequest()
        xhr.open("GET", path, false)
        xhr.send()
        return xhr.responseText
    }

    private fun parseData(data: String): Map<String, List<Recipe>> {
        return data.split("\n").map { lines -> lines.split(";") }.map { line ->
            line[0] to Recipe(
                    inputs = line[1].split(",").map { ingredient ->
                        ingredient.split(":").let { Pair(it[0].toInt(), it[1]) }
                    },
                    outputs = line[2].split(",").map { ingredient ->
                        ingredient.split(":").let { Pair(it[0].toInt(), it[1]) }
                    },
                    time = line[3].toFloat())
        }.groupBy({ it.first }, { it.second })
    }

    private fun createPrototypes(recipes: Map<String, List<Recipe>>): Map<String, Machine> {
        return recipes.map { recipe ->
            Pair(
                    recipe.key,
                    Machine(
                            type = recipe.key,
                            inputsCount = recipe.value.fold(0) { a, e -> max(a, e.inputs.size) },
                            outputsCount = recipe.value.fold(0) { a, e -> max(a, e.outputs.size) }
                    )
            )
        }.toMap()
    }

    fun getMachines(): List<String> {
        return recipes.map { it.key }
    }

    fun getRecipes(machine: String): List<Recipe> {
        return recipes[machine] ?: throw IllegalArgumentException("machine $machine doesn't exist")
    }
}
