
package com.pubtracker.repository

import com.pubtracker.model.User

class UserRepository {

    private val users = mutableMapOf<Int, User>()

    fun add(user: User): Boolean {
        if (users.containsKey(user.id)) {
            return false
        }

        users[user.id] = user
        return true
    }

    fun findById(id: Int): User? {
        return users[id]
    }

    fun findAll(): List<User> {
        return users.values.toList()
    }

    fun update(user: User): Boolean {
        if (!users.containsKey(user.id)) {
            return false
        }

        users[user.id] = user
        return true
    }

    fun delete(id: Int): Boolean {
        return users.remove(id) != null
    }
}
