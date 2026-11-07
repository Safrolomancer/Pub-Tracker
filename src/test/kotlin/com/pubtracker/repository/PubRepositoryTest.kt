package com.pubtracker.com.pubtracker.repository

import com.pubtracker.model.Pub
import com.pubtracker.repository.PubRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PubRepositoryTest {

    @Test
    fun newRepositoryShouldBeEmpty() {
        val repository = PubRepository()

        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    fun addShouldStorePub() {
        val repository = PubRepository()
        val pub = Pub(1, "The Reg", "Waterford")

        val result = repository.add(pub)

        assertTrue(result)
        assertEquals(pub, repository.findById(1))
    }

    @Test
    fun addShouldRejectDuplicateId() {
        val repository = PubRepository()
        val firstPub = Pub(1, "The Reg", "Waterford")
        val secondPub = Pub(1, "Geoff's", "Waterford")

        repository.add(firstPub)
        val result = repository.add(secondPub)

        assertFalse(result)
        assertEquals(firstPub, repository.findById(1))
    }

    @Test
    fun findByIdShouldReturnNullForMissingPub() {
        val repository = PubRepository()

        val result = repository.findById(99)

        assertNull(result)
    }

    @Test
    fun findAllShouldReturnAllPubs() {
        val repository = PubRepository()
        val firstPub = Pub(1, "The Reg", "Waterford")
        val secondPub = Pub(2, "Geoff's", "Waterford")

        repository.add(firstPub)
        repository.add(secondPub)

        val result = repository.findAll()

        assertEquals(2, result.size)
        assertEquals(
            setOf(firstPub, secondPub),
            result.toSet()
        )
    }


    @Test
    fun updateShouldModifyExistingPub() {
        val repository = PubRepository()
        repository.add(Pub(1, "Old Name", "Waterford"))

        val updatedPub = Pub(1, "New Name", "Dublin")

        val result = repository.update(updatedPub)

        assertTrue(result)
        assertEquals(updatedPub, repository.findById(1))
    }

    @Test
    fun updateShouldRejectMissingPub() {
        val repository = PubRepository()

        val result = repository.update(
            Pub(99, "Unknown Pub", "Waterford")
        )

        assertFalse(result)
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    fun deleteShouldRemoveExistingPub() {
        val repository = PubRepository()
        repository.add(Pub(1, "The Reg", "Waterford"))

        val result = repository.delete(1)

        assertTrue(result)
        assertNull(repository.findById(1))
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    fun deleteShouldRejectMissingPub() {
        val repository = PubRepository()

        val result = repository.delete(99)

        assertFalse(result)
    }

}
