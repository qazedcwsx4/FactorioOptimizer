package recipe

import org.w3c.xhr.XMLHttpRequest
import kotlin.js.Json

operator fun Json.get(index: Int): Json {
    return this.asDynamic()[index].unsafeCast<Json>()
}

fun Json.iterator(): Iterator<Json> {
    return this.asDynamic().iterator().unsafeCast<Iterator<Json>>()
}

object ConfigParser {

    fun parseRecipes(path: String): List<Recipe> {
        val config = JSON.parse<Json>(getFile(path))

        return config.iterator().asSequence().map { child ->
            Recipe(
                    name = child["name"].toString(),
                    category = child["category"].toString(),
                    energy = child["energy"].toString().toFloat(),
                    inputs = child["ingredients"].unsafeCast<Json>().iterator().asSequence().map {
                        it["name"].toString() to it["amount"].toString().toInt()
                    }.toList(),
                    outputs = child["products"].unsafeCast<Json>().iterator().asSequence().map {
                        it["name"].toString() to it["amount"].toString().toInt()
                    }.toList()
            )
        }.toList()
    }

    fun parseMachines(path: String): List<MachineData> {
        val config = JSON.parse<Json>(getFile(path))

        return config.iterator().asSequence().map { child ->
            MachineData(
                    name = child["name"].toString(),
                    categories = child["crafting_categories"].unsafeCast<Json>().iterator().asSequence().map {
                        it.toString()
                    }.toList(),
                    speed = child["crafting_speed"].toString().toFloat()
            )
        }.toList()
    }

    private fun getFile(path: String): String {
        val xhr = XMLHttpRequest()
        xhr.open("GET", path, false)
        xhr.send()
        return xhr.responseText
    }
}