package lld.swiggy.models

data class Order(
    val orderId: EntityPublicId,
    val userId: EntityPublicId,
    val restaurantId: EntityPublicId,
    val orderItems: Set<EntityPublicId>, // Set of menuItemIds
    val status: OrderStatus,
    val deliveryAgentId: EntityPublicId?
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

data class OrderRequest(
    val restaurantId: EntityPublicId,
    val orderItems: Set<EntityPublicId>, // Set of menuItemIds
)
