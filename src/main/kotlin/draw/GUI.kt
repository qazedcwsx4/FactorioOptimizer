package draw

import MachineFactory
import org.w3c.dom.*
import org.w3c.dom.events.Event
import recipe.Recipe
import recipe.RecipeRegistry
import kotlin.browser.document
import kotlin.dom.clear

class GUI(
    config: String
) {
    private val machinePicker = document.getElementById("machinePicker") as HTMLSelectElement
    private val recipePicker = document.getElementById("recipePicker") as HTMLSelectElement
    private val quantityPicker = document.getElementById("quantityPicker") as HTMLInputElement

    private val canvas: Canvas
    val chart: Chart

    init {
        val registry = RecipeRegistry(config)
        val factory = MachineFactory(registry)
        chart = Chart(factory, this, "New chart")
        updateMachineList(registry.getMachineNames())

        canvas = Canvas()
    }

    fun updateMachineList(machines: List<String>) {
        machines.forEach {
            machinePicker.add(Option(it))
        }
    }

    fun updateRecipeList(recipes: List<Recipe>) {
        recipePicker.clear()
        recipePicker.add(Option("<none>"))
        recipes.forEach { recipe ->
            recipePicker.add(Option(recipe.name))
        }
    }

    fun updateQuantity(quantity: Int) {
        quantityPicker.value = quantity.toString()
    }

    fun addRecipesListener(type: String, listener: (Event) -> Unit) {
        recipePicker.addEventListener(type, listener)
    }

    fun removeRecipesListener(type: String, listener: (Event) -> Unit) {
        recipePicker.removeEventListener(type, listener)
    }

    fun addQuantityListener(type: String, listener: (Event) -> Unit) {
        quantityPicker.addEventListener(type, listener)
    }

    fun removeQuantityListener(type: String, listener: (Event) -> Unit) {
        quantityPicker.removeEventListener(type, listener)
    }

    fun selectRecipe(index: Int) {
        recipePicker.selectedIndex = index
    }

    fun clearScreen() {
        canvas.context.clearRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())
    }

    fun getCurrentMachine(): String {
        return (machinePicker.selectedOptions[0] as Option).text
    }

    fun getContext(): CanvasRenderingContext2D {
        return canvas.context
    }

    fun addListener(type: String, listener: (Event) -> Unit) {
        canvas.canvas.addEventListener(type, listener)
    }

    fun removeListener(type: String, listener: (Event) -> Unit) {
        canvas.canvas.removeEventListener(type, listener)
    }
}