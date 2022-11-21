package lld.swiggy.models

data class User(
    val userId: EntityPublicId,
    val name: String,
    val location: Location
)

data class UserAddRequest(
    val name: String,
    val location: Location
)