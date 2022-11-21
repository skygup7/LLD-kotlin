package lld.swiggy.models

data class Restaurant(
    val restaurantId: EntityPublicId,
    val name: String,
    val location: Location,
    val rating: Rating,
    val menuItems: Set<EntityPublicId> // Set of menuItemIds
)