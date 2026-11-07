
package com.pubtracker.service

import com.pubtracker.model.Pub
import com.pubtracker.repository.PubRepository

class PubService(
    private val repository: PubRepository
) {

    fun createPub(
        id: Int,
        name: String,
        location: String
    ): Boolean {
        if (id <= 0 || name.isBlank() || location.isBlank()) {
            return false
        }

        val pub = Pub(
            id,
            name.trim(),
            location.trim()
        )

        return repository.add(pub)
    }

    fun getPubById(id: Int): Pub? {
        if (id <= 0) {
            return null
        }

        return repository.findById(id)
    }

    fun getAllPubs(): List<Pub> {
        return repository.findAll()
    }

    fun updatePub(
        id: Int,
        name: String,
        location: String
    ): Boolean {
        if (id <= 0 || name.isBlank() || location.isBlank()) {
            return false
        }

        val updatedPub = Pub(
            id,
            name.trim(),
            location.trim()
        )

        return repository.update(updatedPub)
    }

    fun deletePub(id: Int): Boolean {
        if (id <= 0) {
            return false
        }

        return repository.delete(id)
    }
}
