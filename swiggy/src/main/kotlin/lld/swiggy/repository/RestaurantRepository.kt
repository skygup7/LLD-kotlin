package lld.swiggy.repository

import lld.swiggy.models.EntityPublicId
import lld.swiggy.models.Restaurant

interface RestaurantRepository {

    fun getRestaurantById(restaurantId: EntityPublicId): Restaurant

    fun getAllRestaurants(): List<Restaurant>
}
