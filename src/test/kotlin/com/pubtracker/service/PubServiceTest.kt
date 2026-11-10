package com.pubtracker.service

import com.pubtracker.model.Pub
import com.pubtracker.repository.PubRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import com.pubtracker.repository.VisitRepository

class PubServiceTest {

    @Test
    fun createPubShouldSaveValidPub() {
        val service = PubService(PubRepository(), VisitRepository())

        val result = service.createPub(
            1,
            "The Reg",
            "Waterford"
        )

        assertTrue(result)
        assertEquals(
            Pub(1, "The Reg", "Waterford"),
            service.getPubById(1)
        )
    }

    @Test
    fun createPubShouldTrimWhitespace() {
        val service = PubService(PubRepository(), VisitRepository())

        service.createPub(
            1,
            "  The Reg  ",
            "  Waterford  "
        )

        assertEquals(
            Pub(1, "The Reg", "Waterford"),
            service.getPubById(1)
        )
    }

    @Test
    fun createPubShouldRejectBlankName() {
        val service = PubService(PubRepository(), VisitRepository())

        val result = service.createPub(
            1,
            "   ",
            "Waterford"
        )

        assertFalse(result)
        assertTrue(service.getAllPubs().isEmpty())
    }

    @Test
    fun createPubShouldRejectBlankLocation() {
        val service = PubService(PubRepository(), VisitRepository())

        val result = service.createPub(
            1,
            "The Reg",
            ""
        )

        assertFalse(result)
        assertTrue(service.getAllPubs().isEmpty())
    }

    @Test
    fun createPubShouldRejectInvalidIds() {
        val service = PubService(PubRepository(), VisitRepository())

        assertFalse(
            service.createPub(0, "The Reg", "Waterford")
        )

        assertFalse(
            service.createPub(-1, "The Reg", "Waterford")
        )

        assertTrue(service.getAllPubs().isEmpty())
    }

    @Test
    fun createPubShouldRejectDuplicateId() {
        val service = PubService(PubRepository(), VisitRepository())

        service.createPub(1, "The Reg", "Waterford")

        val result = service.createPub(
            1,
            "Geoff's",
            "Waterford"
        )

        assertFalse(result)
        assertEquals(
            Pub(1, "The Reg", "Waterford"),
            service.getPubById(1)
        )
    }

    @Test
    fun getPubByIdShouldHandleMissingAndInvalidIds() {
        val service = PubService(PubRepository(), VisitRepository())

        assertNull(service.getPubById(99))
        assertNull(service.getPubById(0))
        assertNull(service.getPubById(-1))
    }

    @Test
    fun getAllPubsShouldReturnSavedPubs() {
        val service = PubService(PubRepository(), VisitRepository())

        service.createPub(1, "The Reg", "Waterford")
        service.createPub(2, "Geoff's", "Waterford")

        val pubs = service.getAllPubs()

        assertEquals(2, pubs.size)
        assertEquals(
            setOf(
                Pub(1, "The Reg", "Waterford"),
                Pub(2, "Geoff's", "Waterford")
            ),
            pubs.toSet()
        )
    }

    @Test
    fun updatePubShouldModifyExistingPub() {
        val service = PubService(PubRepository(), VisitRepository())

        service.createPub(1, "Old Name", "Waterford")

        val result = service.updatePub(
            1,
            "  New Name  ",
            "  Dublin  "
        )

        assertTrue(result)
        assertEquals(
            Pub(1, "New Name", "Dublin"),
            service.getPubById(1)
        )
    }

    @Test
    fun updatePubShouldRejectInvalidData() {
        val service = PubService(PubRepository(), VisitRepository())

        service.createPub(1, "The Reg", "Waterford")

        assertFalse(
            service.updatePub(0, "New Name", "Dublin")
        )

        assertFalse(
            service.updatePub(1, "   ", "Dublin")
        )

        assertFalse(
            service.updatePub(1, "New Name", "   ")
        )

        assertEquals(
            Pub(1, "The Reg", "Waterford"),
            service.getPubById(1)
        )
    }

    @Test
    fun updatePubShouldRejectMissingPub() {
        val service = PubService(PubRepository(), VisitRepository())

        val result = service.updatePub(
            99,
            "Unknown Pub",
            "Waterford"
        )

        assertFalse(result)
        assertTrue(service.getAllPubs().isEmpty())
    }

    @Test
    fun deletePubShouldRemoveExistingPub() {
        val service = PubService(PubRepository(), VisitRepository())

        service.createPub(1, "The Reg", "Waterford")

        val result = service.deletePub(1)

        assertTrue(result)
        assertNull(service.getPubById(1))
        assertTrue(service.getAllPubs().isEmpty())
    }

    @Test
    fun deletePubShouldRejectInvalidOrMissingIds() {
        val service = PubService(PubRepository(), VisitRepository())

        assertFalse(service.deletePub(0))
        assertFalse(service.deletePub(-1))
        assertFalse(service.deletePub(99))
    }
}

