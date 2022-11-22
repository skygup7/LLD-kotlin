package lld.swiggy.service

import lld.swiggy.models.DeliveryAgent
import lld.swiggy.models.EntityPublicId
import lld.swiggy.models.OrderRequest
import lld.swiggy.models.OrderStatus

interface SwiggyRestaurantService {

    fun getPendingOrders(restaurantId: EntityPublicId): List<OrderRequest>

    fun changeOrderStatus(orderId: EntityPublicId, toStatus: OrderStatus): Boolean

    fun getDeliveryAgent(orderId: EntityPublicId): DeliveryAgent
}
