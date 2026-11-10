package com.pubtracker.service

import com.pubtracker.model.Visit
import com.pubtracker.repository.PubRepository
import com.pubtracker.repository.UserRepository
import com.pubtracker.repository.VisitRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class VisitServiceTest {

    private data class TestSetup(
        val visits: VisitService,
        val users: UserService,
        val pubs: PubService
    )


    private fun createSetup(): TestSetup {
        val visitRepository = VisitRepository()

        val userService = UserService(
            UserRepository(),
            visitRepository
        )

        val pubService = PubService(
            PubRepository(),
            visitRepository
        )

        val visitService = VisitService(
            visitRepository,
            userService,
            pubService
        )

        return TestSetup(
            visitService,
            userService,
            pubService
        )
    }

    @Test
    fun createVisitShouldSaveValidVisit() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")

        val result = setup.visits.createVisit(1, 1, 3)

        assertTrue(result)
        assertEquals(
            Visit(1, 1, 3),
            setup.visits.getVisitById(1)
        )
    }

    @Test
    fun createVisitShouldRejectInvalidIds() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")

        assertFalse(setup.visits.createVisit(0, 1, 3))
        assertFalse(setup.visits.createVisit(-1, 1, 3))
        assertFalse(setup.visits.createVisit(1, 0, 3))
        assertFalse(setup.visits.createVisit(1, 1, -3))

        assertTrue(setup.visits.getAllVisits().isEmpty())
    }

    @Test
    fun createVisitShouldRejectMissingUser() {
        val setup = createSetup()

        setup.pubs.createPub(3, "The Reg", "Waterford")

        val result = setup.visits.createVisit(1, 99, 3)

        assertFalse(result)
        assertTrue(setup.visits.getAllVisits().isEmpty())
    }

    @Test
    fun createVisitShouldRejectMissingPub() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")

        val result = setup.visits.createVisit(1, 1, 99)

        assertFalse(result)
        assertTrue(setup.visits.getAllVisits().isEmpty())
    }

    @Test
    fun createVisitShouldRejectDuplicateId() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")

        assertTrue(setup.visits.createVisit(1, 1, 3))

        val result = setup.visits.createVisit(1, 1, 3)

        assertFalse(result)
        assertEquals(1, setup.visits.getAllVisits().size)
    }

    @Test
    fun createVisitShouldAllowRepeatedVisits() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")

        assertTrue(setup.visits.createVisit(1, 1, 3))
        assertTrue(setup.visits.createVisit(2, 1, 3))

        assertEquals(2, setup.visits.getAllVisits().size)
    }

    @Test
    fun getVisitByIdShouldHandleMissingAndInvalidIds() {
        val setup = createSetup()

        assertNull(setup.visits.getVisitById(99))
        assertNull(setup.visits.getVisitById(0))
        assertNull(setup.visits.getVisitById(-1))
    }

    @Test
    fun getAllVisitsShouldReturnSavedVisits() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")
        setup.pubs.createPub(4, "Tullys Bar", "Waterford")

        setup.visits.createVisit(1, 1, 3)
        setup.visits.createVisit(2, 1, 4)

        assertEquals(
            setOf(Visit(1, 1, 3), Visit(2, 1, 4)),
            setup.visits.getAllVisits().toSet()
        )
    }

    @Test
    fun getVisitsByUserIdShouldFilterVisits() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.users.createUser(2, "Bob")
        setup.pubs.createPub(3, "The Reg", "Waterford")

        setup.visits.createVisit(1, 1, 3)
        setup.visits.createVisit(2, 2, 3)

        assertEquals(
            listOf(Visit(1, 1, 3)),
            setup.visits.getVisitsByUserId(1)
        )
    }

    @Test
    fun getVisitsByPubIdShouldFilterVisits() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")
        setup.pubs.createPub(4, "Tullys Bar", "Waterford")

        setup.visits.createVisit(1, 1, 3)
        setup.visits.createVisit(2, 1, 4)

        assertEquals(
            listOf(Visit(2, 1, 4)),
            setup.visits.getVisitsByPubId(4)
        )
    }

    @Test
    fun filteringShouldHandleInvalidAndMissingIds() {
        val setup = createSetup()

        assertTrue(setup.visits.getVisitsByUserId(0).isEmpty())
        assertTrue(setup.visits.getVisitsByUserId(99).isEmpty())
        assertTrue(setup.visits.getVisitsByPubId(-1).isEmpty())
        assertTrue(setup.visits.getVisitsByPubId(99).isEmpty())
    }

    @Test
    fun deleteVisitShouldRemoveExistingVisit() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")
        setup.visits.createVisit(1, 1, 3)

        val result = setup.visits.deleteVisit(1)

        assertTrue(result)
        assertNull(setup.visits.getVisitById(1))
        assertTrue(setup.visits.getAllVisits().isEmpty())
    }

    @Test
    fun deleteVisitShouldRejectMissingOrInvalidIds() {
        val setup = createSetup()

        assertFalse(setup.visits.deleteVisit(0))
        assertFalse(setup.visits.deleteVisit(-1))
        assertFalse(setup.visits.deleteVisit(99))
    }


    @Test
    fun deleteUserShouldRejectUserWithVisits() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")
        setup.visits.createVisit(1, 1, 3)

        val result = setup.visits.deleteUser(1)

        assertFalse(result)
        assertEquals("Alice", setup.users.getUserById(1)?.name)
        assertEquals(Visit(1, 1, 3), setup.visits.getVisitById(1))
    }

    @Test
    fun deletePubShouldRejectPubWithVisits() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")
        setup.visits.createVisit(1, 1, 3)

        val result = setup.visits.deletePub(3)

        assertFalse(result)
        assertEquals("The Reg", setup.pubs.getPubById(3)?.name)
        assertEquals(Visit(1, 1, 3), setup.visits.getVisitById(1))
    }

    @Test
    fun deleteUserShouldSucceedAfterVisitsRemoved() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")
        setup.visits.createVisit(1, 1, 3)

        assertTrue(setup.visits.deleteVisit(1))
        assertTrue(setup.visits.deleteUser(1))
        assertNull(setup.users.getUserById(1))
    }

    @Test
    fun deletePubShouldSucceedAfterVisitsRemoved() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")
        setup.visits.createVisit(1, 1, 3)

        assertTrue(setup.visits.deleteVisit(1))
        assertTrue(setup.visits.deletePub(3))
        assertNull(setup.pubs.getPubById(3))
    }


    @Test
    fun deletingPubDirectlyShouldNotLeaveOrphanVisit() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")
        setup.visits.createVisit(1, 1, 3)

        val result = setup.pubs.deletePub(3)

        assertFalse(result)
        assertEquals(
            "The Reg",
            setup.pubs.getPubById(3)?.name
        )
        assertEquals(
            Visit(1, 1, 3),
            setup.visits.getVisitById(1)
        )
    }


    @Test
    fun deletingUserDirectlyShouldNotLeaveOrphanVisit() {
        val setup = createSetup()

        setup.users.createUser(1, "Alice")
        setup.pubs.createPub(3, "The Reg", "Waterford")
        setup.visits.createVisit(1, 1, 3)

        val result = setup.users.deleteUser(1)

        assertFalse(result)
        assertEquals(
            "Alice",
            setup.users.getUserById(1)?.name
        )
        assertEquals(
            Visit(1, 1, 3),
            setup.visits.getVisitById(1)
        )
    }

}
