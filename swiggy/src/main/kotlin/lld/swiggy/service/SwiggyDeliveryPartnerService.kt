package lld.swiggy.service

import lld.swiggy.models.EntityPublicId
import lld.swiggy.models.Location
import lld.swiggy.models.Order
import lld.swiggy.models.OrderStatus

interface SwiggyDeliveryPartnerService {

    fun updateLocation(deliveryAgentId: EntityPublicId, location: Location): Boolean

    fun getOrderToDeliver(deliveryAgentId: EntityPublicId): Order

    fun changeOrderStatus(orderId: EntityPublicId, toStatus: OrderStatus): Boolean
}
