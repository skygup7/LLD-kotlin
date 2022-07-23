package lld.splitwise.repository.impls

import lld.splitwise.models.User
import lld.splitwise.repository.UserRepository

class UserRepositoryInMemImpl: UserRepository {

    private val users: MutableMap<String, User> = mutableMapOf()

    override fun addUser(user: User): Boolean {
        if (users.containsKey(user.id)) return false

        users[user.id] = user
        return true
    }

    override fun updateUserDetails(user: User): Boolean {
        if (!users.containsKey(user.id)) return false

        users[user.id] = user
        return true
    }

    override fun removeUser(userId: String): Boolean {
        return users.remove(userId) != null
    }

    override fun getUser(userId: String): User? {
        return users[userId]
    }
}