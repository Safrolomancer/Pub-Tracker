package com.pubtracker.repository

import com.pubtracker.model.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserRepositoryTest {

    @Test
    fun newRepositoryShouldBeEmpty() {
        val repository = UserRepository()

        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    fun addShouldStoreUser() {
        val repository = UserRepository()
        val user = User(1, "Alice")

        val result = repository.add(user)

        assertTrue(result)
        assertEquals(user, repository.findById(1))
    }

    @Test
    fun addShouldRejectDuplicateId() {
        val repository = UserRepository()
        val firstUser = User(1, "Alice")
        val secondUser = User(1, "Bob")

        repository.add(firstUser)
        val result = repository.add(secondUser)

        assertFalse(result)
        assertEquals(firstUser, repository.findById(1))
    }

    @Test
    fun findByIdShouldReturnNullForMissingUser() {
        val repository = UserRepository()

        assertNull(repository.findById(99))
    }

    @Test
    fun findAllShouldReturnAllUsers() {
        val repository = UserRepository()
        val firstUser = User(1, "Alice")
        val secondUser = User(2, "Bob")

        repository.add(firstUser)
        repository.add(secondUser)

        val users = repository.findAll()

        assertEquals(2, users.size)
        assertEquals(
            setOf(firstUser, secondUser),
            users.toSet()
        )
    }

    @Test
    fun updateShouldModifyExistingUser() {
        val repository = UserRepository()
        repository.add(User(1, "Alice"))

        val updatedUser = User(1, "Alice Smith")
        val result = repository.update(updatedUser)

        assertTrue(result)
        assertEquals(updatedUser, repository.findById(1))
    }

    @Test
    fun updateShouldRejectMissingUser() {
        val repository = UserRepository()

        val result = repository.update(User(99, "Unknown"))

        assertFalse(result)
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    fun deleteShouldRemoveExistingUser() {
        val repository = UserRepository()
        repository.add(User(1, "Alice"))

        val result = repository.delete(1)

        assertTrue(result)
        assertNull(repository.findById(1))
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    fun deleteShouldRejectMissingUser() {
        val repository = UserRepository()

        val result = repository.delete(99)

        assertFalse(result)
    }
}
