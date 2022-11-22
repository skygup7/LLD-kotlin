package lld.swiggy.repository

import lld.swiggy.models.EntityPublicId
import lld.swiggy.models.Order

interface OrderRepository {

    fun addOrder(order: Order): Order

    fun getAllOrdersFor(userId: EntityPublicId): List<Order>

    fun getOrderById(orderId: EntityPublicId): Order
}
