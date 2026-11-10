package com.pubtracker.repository

import com.pubtracker.model.Visit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class VisitRepositoryTest {

    @Test
    fun newRepositoryShouldBeEmpty() {
        val repository = VisitRepository()

        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    fun addShouldStoreVisit() {
        val repository = VisitRepository()
        val visit = Visit(1, 1, 3)

        val result = repository.add(visit)

        assertTrue(result)
        assertEquals(visit, repository.findById(1))
    }

    @Test
    fun addShouldRejectDuplicateId() {
        val repository = VisitRepository()
        val firstVisit = Visit(1, 1, 3)
        val secondVisit = Visit(1, 2, 4)

        repository.add(firstVisit)
        val result = repository.add(secondVisit)

        assertFalse(result)
        assertEquals(firstVisit, repository.findById(1))
    }

    @Test
    fun addShouldAllowRepeatedVisitsWithDifferentIds() {
        val repository = VisitRepository()
        val firstVisit = Visit(1, 1, 3)
        val secondVisit = Visit(2, 1, 3)

        assertTrue(repository.add(firstVisit))
        assertTrue(repository.add(secondVisit))

        assertEquals(2, repository.findAll().size)
    }

    @Test
    fun findByIdShouldReturnNullForMissingVisit() {
        val repository = VisitRepository()

        assertNull(repository.findById(99))
    }

    @Test
    fun findAllShouldReturnAllVisits() {
        val repository = VisitRepository()
        val firstVisit = Visit(1, 1, 3)
        val secondVisit = Visit(2, 2, 4)

        repository.add(firstVisit)
        repository.add(secondVisit)

        val visits = repository.findAll()

        assertEquals(2, visits.size)
        assertEquals(
            setOf(firstVisit, secondVisit),
            visits.toSet()
        )
    }

    @Test
    fun findByUserIdShouldReturnMatchingVisits() {
        val repository = VisitRepository()

        val firstVisit = Visit(1, 1, 3)
        val secondVisit = Visit(2, 1, 4)
        val thirdVisit = Visit(3, 2, 3)

        repository.add(firstVisit)
        repository.add(secondVisit)
        repository.add(thirdVisit)

        val result = repository.findByUserId(1)

        assertEquals(
            setOf(firstVisit, secondVisit),
            result.toSet()
        )
        assertEquals(2, result.size)
    }

    @Test
    fun findByPubIdShouldReturnMatchingVisits() {
        val repository = VisitRepository()

        val firstVisit = Visit(1, 1, 3)
        val secondVisit = Visit(2, 2, 3)
        val thirdVisit = Visit(3, 1, 4)

        repository.add(firstVisit)
        repository.add(secondVisit)
        repository.add(thirdVisit)

        val result = repository.findByPubId(3)

        assertEquals(
            setOf(firstVisit, secondVisit),
            result.toSet()
        )
        assertEquals(2, result.size)
    }

    @Test
    fun filteringShouldReturnEmptyListForMissingIds() {
        val repository = VisitRepository()

        repository.add(Visit(1, 1, 3))

        assertTrue(repository.findByUserId(99).isEmpty())
        assertTrue(repository.findByPubId(99).isEmpty())
    }

    @Test
    fun deleteShouldRemoveExistingVisit() {
        val repository = VisitRepository()
        repository.add(Visit(1, 1, 3))

        val result = repository.delete(1)

        assertTrue(result)
        assertNull(repository.findById(1))
        assertTrue(repository.findAll().isEmpty())
    }

    @Test
    fun deleteShouldRejectMissingVisit() {
        val repository = VisitRepository()

        val result = repository.delete(99)

        assertFalse(result)
    }


}
