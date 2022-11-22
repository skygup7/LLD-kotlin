package lld.swiggy.service

import lld.swiggy.models.EntityPublicId
import lld.swiggy.models.Location
import lld.swiggy.models.MenuItem
import lld.swiggy.models.Order
import lld.swiggy.models.OrderRequest
import lld.swiggy.models.OrderStatus
import lld.swiggy.models.Restaurant
import lld.swiggy.models.User
import lld.swiggy.models.UserAddRequest

interface SwiggyUserService {

    fun addUser(userAddRequest: UserAddRequest): User

    fun getRestaurantsAround(location: Location, withinRadius: Double): List<Restaurant>

    fun getMenuItemsForRestaurant(restaurantId: EntityPublicId): List<MenuItem>

    fun placeOrder(userId: EntityPublicId, orderRequest: OrderRequest): Order

    fun getOrderListForUser(userId: EntityPublicId): List<Order>

    fun getOrderById(orderId: EntityPublicId): Order

    fun getOrderStatusAndLocationById(orderId: EntityPublicId): Pair<OrderStatus, Location>
}
