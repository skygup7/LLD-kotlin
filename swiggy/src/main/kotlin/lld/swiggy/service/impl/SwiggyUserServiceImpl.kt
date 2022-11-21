package lld.swiggy.service.impl

import lld.swiggy.models.EntityPrefix
import lld.swiggy.models.EntityPublicId
import lld.swiggy.models.Location
import lld.swiggy.models.MenuItem
import lld.swiggy.models.Order
import lld.swiggy.models.OrderRequest
import lld.swiggy.models.OrderStatus
import lld.swiggy.models.Restaurant
import lld.swiggy.models.User
import lld.swiggy.models.UserAddRequest
import lld.swiggy.repository.DeliveryAgentRepository
import lld.swiggy.repository.MenuItemRepository
import lld.swiggy.repository.OrderRepository
import lld.swiggy.repository.RestaurantRepository
import lld.swiggy.repository.UserRepository
import lld.swiggy.service.SwiggyUserService
import org.slf4j.LoggerFactory
import java.util.*

class SwiggyUserServiceImpl(
    private val userRepository: UserRepository,
    private val restaurantRepository: RestaurantRepository,
    private val menuItemRepository: MenuItemRepository,
    private val orderRepository: OrderRepository,
    private val deliveryAgentRepository: DeliveryAgentRepository
): SwiggyUserService {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SwiggyUserServiceImpl::class.java)
    }

    override fun addUser(userAddRequest: UserAddRequest): User {
        val user = User(
            userId = EntityPublicId(
                prefix = EntityPrefix.user,
                publicId = UUID.randomUUID().toString()
            ),
            name = userAddRequest.name,
            location = userAddRequest.location
        )

        return userRepository.addUser(user).also {
            LOGGER.info("user Id for ${userAddRequest.name} created successfully")
        }
    }

    override fun getRestaurantsAround(location: Location, withinRadius: Double): List<Restaurant> {
        val allRestaurants = restaurantRepository.getAllRestaurants()

        return allRestaurants.filter { location.calculateDistanceFrom(it.location) <= withinRadius }.also {
            LOGGER.info("Found ${it.size} restaurants within search radius")
        }
    }

    override fun getMenuItemsForRestaurant(restaurantId: EntityPublicId): List<MenuItem> {
        val restaurant = restaurantRepository.getRestaurantById(restaurantId)

        return menuItemRepository.getMenuItemsByPublicIds(restaurant.menuItems)
    }

    override fun placeOrder(userId: EntityPublicId, orderRequest: OrderRequest): Order {
        val newOrder = Order(
            orderId = EntityPublicId(
                prefix = EntityPrefix.order,
                publicId = UUID.randomUUID().toString()
            ),
            userId = userId,
            restaurantId = orderRequest.restaurantId,
            orderItems = orderRequest.orderItems,
            status = OrderStatus.OrderPlaced,
            deliveryAgentId = null
        )

        return orderRepository.addOrder(newOrder).also {
            LOGGER.info("Order with order-id: '${it.orderId}' placed successfully.")
        }
    }

    override fun getOrderListForUser(userId: EntityPublicId): List<Order> {
        return orderRepository.getAllOrdersFor(userId)
    }

    override fun getOrderById(orderId: EntityPublicId): Order {
        return orderRepository.getOrderById(orderId)
    }

    override fun getOrderStatusAndLocationById(orderId: EntityPublicId): Pair<OrderStatus, Location> {
        val order = orderRepository.getOrderById(orderId)

        return when (order.status) {
            OrderStatus.OrderPlaced, OrderStatus.OrderAccepted, OrderStatus.OrderInPreparation, OrderStatus.OrderPrepared -> {
                Pair(order.status, restaurantRepository.getRestaurantById(order.restaurantId).location)
            }
            OrderStatus.OrderPickedUp -> {
                Pair(order.status, deliveryAgentRepository.getDeliveryAgentById(order.deliveryAgentId!!).location)
            }
            OrderStatus.OrderDelivered, OrderStatus.OrderCancelled -> {
                Pair(order.status, userRepository.getUserById(order.userId).location)
            }
        }
    }
}