package lld.swiggy.repository

import lld.swiggy.models.EntityPublicId
import lld.swiggy.models.User

interface UserRepository {

    fun addUser(user: User): User

    fun getUserById(userId: EntityPublicId): User
}