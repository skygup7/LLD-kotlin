package lld.swiggy.models

data class MenuItem(
    val menuItemId: EntityPublicId,
    val name: String,
    val description: String,
    val rating: Rating
)