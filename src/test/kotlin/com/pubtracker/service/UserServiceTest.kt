package com.pubtracker.service

import com.pubtracker.model.User
import com.pubtracker.repository.UserRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.pubtracker.repository.VisitRepository

class UserServiceTest {

    @Test
    fun createUserShouldSaveValidUser() {
        val service = UserService(UserRepository(), VisitRepository())

        val result = service.createUser(1, "Alice")

        assertTrue(result)
        assertEquals(
            User(1, "Alice"),
            service.getUserById(1)
        )
    }

    @Test
    fun createUserShouldTrimWhitespace() {
        val service = UserService(UserRepository(), VisitRepository())

        val result = service.createUser(1, "  Alice  ")

        assertTrue(result)
        assertEquals(
            User(1, "Alice"),
            service.getUserById(1)
        )
    }

    @Test
    fun createUserShouldRejectBlankName() {
        val service = UserService(UserRepository(), VisitRepository())

        val result = service.createUser(1, "   ")

        assertFalse(result)
        assertTrue(service.getAllUsers().isEmpty())
    }

    @Test
    fun createUserShouldRejectInvalidIds() {
        val service = UserService(UserRepository(), VisitRepository())

        assertFalse(service.createUser(0, "Alice"))
        assertFalse(service.createUser(-1, "Alice"))

        assertTrue(service.getAllUsers().isEmpty())
    }

    @Test
    fun createUserShouldRejectDuplicateId() {
        val service = UserService(UserRepository(), VisitRepository())

        service.createUser(1, "Alice")

        val result = service.createUser(1, "Bob")

        assertFalse(result)
        assertEquals(
            User(1, "Alice"),
            service.getUserById(1)
        )
    }

    @Test
    fun getUserByIdShouldReturnExistingUser() {
        val service = UserService(UserRepository(), VisitRepository())

        service.createUser(1, "Alice")

        assertEquals(
            User(1, "Alice"),
            service.getUserById(1)
        )
    }

    @Test
    fun getUserByIdShouldHandleMissingAndInvalidIds() {
        val service = UserService(UserRepository(), VisitRepository())

        assertNull(service.getUserById(99))
        assertNull(service.getUserById(0))
        assertNull(service.getUserById(-1))
    }

    @Test
    fun getAllUsersShouldReturnSavedUsers() {
        val service = UserService(UserRepository(), VisitRepository())

        service.createUser(1, "Alice")
        service.createUser(2, "Bob")

        val users = service.getAllUsers()

        assertEquals(2, users.size)
        assertEquals(
            setOf(
                User(1, "Alice"),
                User(2, "Bob")
            ),
            users.toSet()
        )
    }

    @Test
    fun updateUserShouldModifyExistingUser() {
        val service = UserService(UserRepository(), VisitRepository())

        service.createUser(1, "Alice")

        val result = service.updateUser(1, "  Alice Smith  ")

        assertTrue(result)
        assertEquals(
            User(1, "Alice Smith"),
            service.getUserById(1)
        )
    }

    @Test
    fun updateUserShouldRejectInvalidData() {
        val service = UserService(UserRepository(), VisitRepository())
        service.createUser(1, "Alice")

        assertFalse(service.updateUser(0, "Bob"))
        assertFalse(service.updateUser(-1, "Bob"))
        assertFalse(service.updateUser(1, "   "))

        assertEquals(
            User(1, "Alice"),
            service.getUserById(1)
        )
    }

    @Test
    fun updateUserShouldRejectMissingUser() {
        val service = UserService(UserRepository(), VisitRepository())

        val result = service.updateUser(99, "Bob")

        assertFalse(result)
        assertTrue(service.getAllUsers().isEmpty())
    }

    @Test
    fun deleteUserShouldRemoveExistingUser() {
        val service = UserService(UserRepository(), VisitRepository())

        service.createUser(1, "Alice")

        val result = service.deleteUser(1)

        assertTrue(result)
        assertNull(service.getUserById(1))
        assertTrue(service.getAllUsers().isEmpty())
    }

    @Test
    fun deleteUserShouldRejectInvalidOrMissingIds() {
        val service = UserService(UserRepository(), VisitRepository())

        assertFalse(service.deleteUser(0))
        assertFalse(service.deleteUser(-1))
        assertFalse(service.deleteUser(99))
    }
}
