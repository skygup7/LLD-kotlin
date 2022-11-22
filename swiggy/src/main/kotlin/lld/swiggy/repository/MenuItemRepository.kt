package lld.swiggy.repository

import lld.swiggy.models.EntityPublicId
import lld.swiggy.models.MenuItem

interface MenuItemRepository {

    fun getMenuItemsByPublicIds(menuItemIds: Set<EntityPublicId>): List<MenuItem>
}
