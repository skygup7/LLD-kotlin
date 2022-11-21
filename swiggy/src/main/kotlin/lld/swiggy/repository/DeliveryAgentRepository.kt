package lld.swiggy.repository

import lld.swiggy.models.DeliveryAgent
import lld.swiggy.models.EntityPublicId

interface DeliveryAgentRepository {

    fun getDeliveryAgentById(deliveryAgentId: EntityPublicId): DeliveryAgent
}