package lld.swiggy.models

import kotlin.math.pow
import kotlin.math.sqrt

data class Location(
    val x: Double,
    val y: Double
) {
    fun calculateDistanceFrom(location: Location): Double {
        return sqrt((location.x - x).pow(2) + (location.y - y).pow(2))
    }
}

enum class EntityPrefix {
    user,
    restaurant,
    menuItem,
    deliveryAgent,
    order
}

data class EntityPublicId(
    val prefix: EntityPrefix,
    val publicId: String
) {
    override fun toString(): String {
        return prefix.name + ":" + publicId
    }
}

data class Rating(
    val avgRating: Double,
    val ratingCount: Long
) {
    fun calculateNewRating(rating: Long): Rating {
        return Rating(
            avgRating = (avgRating * ratingCount + rating) / (ratingCount + 1),
            ratingCount = ratingCount + 1
        )
    }
}