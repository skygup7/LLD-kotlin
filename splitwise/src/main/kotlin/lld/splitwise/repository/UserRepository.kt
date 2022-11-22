package lld.splitwise.repository

import lld.splitwise.models.User

interface UserRepository {

    /* adds user to the repository and returns boolean for if the user was added successfully */
    fun addUser(user: User): Boolean

    /**
     *  updates user details in the repository and returns boolean for if the user was updated successfully.
     *  Also returns false if user is not found originally
     */
    fun updateUserDetails(user: User): Boolean

    /**
     * removes the user from the repository by [userId]
     *
     * @return false if user is not found
     */
    fun removeUser(userId: String): Boolean

    /**
     * fetches the user by [userId]
     *
     * @return null if user is not found
     */
    fun getUser(userId: String): User?
}
