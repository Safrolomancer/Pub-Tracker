
package com.pubtracker.repository

import com.pubtracker.model.Pub

class PubRepository {

    private val pubs = mutableMapOf<Int, Pub>()

    fun add(pub: Pub): Boolean {
        if (pubs.containsKey(pub.id)) {
            return false
        }

        pubs[pub.id] = pub
        return true
    }

    fun findById(id: Int): Pub? {
        return pubs[id]
    }

    fun findAll(): List<Pub> {
        return pubs.values.toList()
    }

    fun update(pub: Pub): Boolean {
        if (!pubs.containsKey(pub.id)) {
            return false
        }

        pubs[pub.id] = pub
        return true
    }

    fun delete(id: Int): Boolean {
        return pubs.remove(id) != null
    }
}
