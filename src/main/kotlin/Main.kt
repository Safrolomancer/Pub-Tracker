import com.pubtracker.repository.PubRepository
import com.pubtracker.service.PubService
import com.pubtracker.ui.PubConsoleUI

fun main() {
    val repository = PubRepository()
    val service = PubService(repository)
    val ui = PubConsoleUI(service)

    ui.start()
}

