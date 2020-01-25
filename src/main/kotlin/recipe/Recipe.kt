package recipe

data class Recipe(
        val name: String,
        val category: String,
        val energy: Float,
        val inputs: List<Pair<String, Int>>,
        val outputs: List<Pair<String, Int>>
)