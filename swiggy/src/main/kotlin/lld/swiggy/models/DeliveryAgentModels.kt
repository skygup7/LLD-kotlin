package lld.swiggy.models

data class DeliveryAgent(
    val deliveryAgentId: EntityPublicId,
    val name: String,
    val location: Location,
    val status: DeliveryAgentStatus,
    val orderId: EntityPublicId?
)

enum class DeliveryAgentStatus {
    Assigned,
    Free
}