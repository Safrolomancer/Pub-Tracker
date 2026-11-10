package com.pubtracker.repository

import com.pubtracker.model.Visit

class VisitRepository {

    private val visits = mutableMapOf<Int, Visit>()

    fun add(visit: Visit): Boolean {
        if (visits.containsKey(visit.id)) {
            return false
        }

        visits[visit.id] = visit
        return true
    }

    fun findById(id: Int): Visit? {
        return visits[id]
    }

    fun findAll(): List<Visit> {
        return visits.values.toList()
    }

    fun findByUserId(userId: Int): List<Visit> {
        return visits.values.filter { visit ->
            visit.userId == userId
        }
    }

    fun findByPubId(pubId: Int): List<Visit> {
        return visits.values.filter { visit ->
            visit.pubId == pubId
        }
    }

    fun delete(id: Int): Boolean {
        return visits.remove(id) != null
    }

}
