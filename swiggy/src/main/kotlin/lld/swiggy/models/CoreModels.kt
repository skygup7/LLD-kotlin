package lld.swiggy.models

data class Location(
    val x: Long,
    val y: Long
)

enum class EntityPrefix {
    user,
    restaurant,
    menuItem,
    deliveryAgent
}

data class EntityPublicId(
    val prefix: EntityPrefix,
    val publicId: String
) {
    override fun toString(): String {
        return prefix.name + publicId
    }
}

data class Rating(
    val avgRating: Double,
    val ratingCount: Long
)