import com.pubtracker.repository.PubRepository
import com.pubtracker.repository.VisitRepository
import com.pubtracker.service.PubService
import com.pubtracker.ui.PubConsoleUI

fun main() {
    val repository = PubRepository()
    val visitRepository = VisitRepository()

    val service = PubService(
        repository,
        visitRepository
    )

    val ui = PubConsoleUI(service)

    ui.start()
}
