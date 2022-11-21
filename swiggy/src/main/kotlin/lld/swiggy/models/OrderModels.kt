package lld.swiggy.models

data class Order(
    val orderId: EntityPublicId,
    val userId: EntityPublicId,
    val restaurantId: EntityPublicId,
    val orderItems: Set<String>, // Set of menuItemIds
    val status: OrderStatus
)

enum class OrderStatus {
    OrderPlaced,
    OrderAccepted,
    OrderInPreparation,
    OrderPrepared,
    OrderPickedUp,
    OrderDelivered,
    OrderCancelled
}