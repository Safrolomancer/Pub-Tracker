package com.pubtracker.ui

import com.pubtracker.service.PubService

class PubConsoleUI(
    private val pubService: PubService
) {

    fun start() {
        println("Welcome to Pub Tracker!")

        while (true) {
            printMenu()

            when (readlnOrNull()?.trim()) {
                "1" -> createPub()
                "2" -> listPubs()
                "3" -> viewPub()
                "4" -> updatePub()
                "5" -> deletePub()
                "0" -> {
                    println("Goodbye!")
                    return
                }
                null -> {
                    println("Input closed.")
                    return
                }
                else -> println("Invalid menu option.")
            }
        }
    }

    private fun printMenu() {
        println()
        println("========== PUB TRACKER ==========")
        println("1. Add pub")
        println("2. List all pubs")
        println("3. Find pub by ID")
        println("4. Update pub")
        println("5. Delete pub")
        println("0. Exit")
        println("=================================")
        print("Choose an option: ")
    }

    private fun createPub() {
        val id = readId() ?: return
        val name = readInput("Name: ") ?: return
        val location = readInput("Location: ") ?: return

        if (pubService.createPub(id, name, location)) {
            println("Pub created successfully.")
        } else {
            println("Could not create pub. Check the details or ID.")
        }
    }

    private fun listPubs() {
        val pubs = pubService.getAllPubs()

        if (pubs.isEmpty()) {
            println("No pubs found.")
            return
        }

        println("\n--- All Pubs ---")

        pubs.sortedBy { it.id }.forEach { pub ->
            println("${pub.id}. ${pub.name} - ${pub.location}")
        }
    }

    private fun viewPub() {
        val id = readId() ?: return
        val pub = pubService.getPubById(id)

        if (pub == null) {
            println("Pub not found.")
            return
        }

        println("\n--- Pub Details ---")
        println("ID: ${pub.id}")
        println("Name: ${pub.name}")
        println("Location: ${pub.location}")
    }

    private fun updatePub() {
        val id = readId() ?: return

        if (pubService.getPubById(id) == null) {
            println("Pub not found.")
            return
        }

        val name = readInput("New name: ") ?: return
        val location = readInput("New location: ") ?: return

        if (pubService.updatePub(id, name, location)) {
            println("Pub updated successfully.")
        } else {
            println("Could not update pub. Check the details.")
        }
    }

    private fun deletePub() {
        val id = readId() ?: return

        if (pubService.deletePub(id)) {
            println("Pub deleted successfully.")
        } else {
            println("Pub not found.")
        }
    }

    private fun readId(): Int? {
        val input = readInput("Pub ID: ") ?: return null
        val id = input.trim().toIntOrNull()

        if (id == null || id <= 0) {
            println("Invalid ID. Enter a positive whole number.")
            return null
        }

        return id
    }

    private fun readInput(prompt: String): String? {
        print(prompt)
        return readlnOrNull()
    }
}
