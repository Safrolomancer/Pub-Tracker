package com.pubtracker.service

import com.pubtracker.model.Visit
import com.pubtracker.repository.VisitRepository

class VisitService(
    private val repository: VisitRepository,
    private val userService: UserService,
    private val pubService: PubService
) {

    fun createVisit(
        id: Int,
        userId: Int,
        pubId: Int
    ): Boolean {
        if (id <= 0 || userId <= 0 || pubId <= 0) {
            return false
        }

        if (userService.getUserById(userId) == null) {
            return false
        }

        if (pubService.getPubById(pubId) == null) {
            return false
        }

        val visit = Visit(id, userId, pubId)

        return repository.add(visit)
    }

    fun getVisitById(id: Int): Visit? {
        if (id <= 0) {
            return null
        }

        return repository.findById(id)
    }

    fun getAllVisits(): List<Visit> {
        return repository.findAll()
    }

    fun getVisitsByUserId(userId: Int): List<Visit> {
        if (userId <= 0) {
            return emptyList()
        }

        return repository.findByUserId(userId)
    }

    fun getVisitsByPubId(pubId: Int): List<Visit> {
        if (pubId <= 0) {
            return emptyList()
        }

        return repository.findByPubId(pubId)
    }

    fun deleteVisit(id: Int): Boolean {
        if (id <= 0) {
            return false
        }

        return repository.delete(id)
    }


    fun deleteUser(userId: Int): Boolean {
        if (userId <= 0) {
            return false
        }

        if (repository.findByUserId(userId).isNotEmpty()) {
            return false
        }

        return userService.deleteUser(userId)
    }

    fun deletePub(pubId: Int): Boolean {
        if (pubId <= 0) {
            return false
        }

        if (repository.findByPubId(pubId).isNotEmpty()) {
            return false
        }

        return pubService.deletePub(pubId)
    }

}
