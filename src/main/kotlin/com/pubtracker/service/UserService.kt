package com.pubtracker.service

import com.pubtracker.model.User
import com.pubtracker.repository.UserRepository

class UserService(
    private val repository: UserRepository
) {

    fun createUser(id: Int, name: String): Boolean {
        if (id <= 0 || name.isBlank()) {
            return false
        }

        val user = User(
            id,
            name.trim()
        )

        return repository.add(user)
    }

    fun getUserById(id: Int): User? {
        if (id <= 0) {
            return null
        }

        return repository.findById(id)
    }

    fun getAllUsers(): List<User> {
        return repository.findAll()
    }

    fun updateUser(id: Int, name: String): Boolean {
        if (id <= 0 || name.isBlank()) {
            return false
        }

        val updatedUser = User(
            id,
            name.trim()
        )

        return repository.update(updatedUser)
    }

    fun deleteUser(id: Int): Boolean {
        if (id <= 0) {
            return false
        }

        return repository.delete(id)
    }
}
