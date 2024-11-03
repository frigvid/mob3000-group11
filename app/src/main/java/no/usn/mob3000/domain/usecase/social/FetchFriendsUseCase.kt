package no.usn.mob3000.domain.usecase.social

/**
 * @author Hussein Abdul-Ameer
 * created on 03.11.2024
 */
import no.usn.mob3000.data.repository.social.FriendsRepository
import no.usn.mob3000.domain.model.DocsData
import no.usn.mob3000.domain.model.Friend

class FetchFriendsUseCase(private val fetchRepository: FriendsRepository = FriendsRepository()) {
    suspend fun fetchFriends(): Result<List<Friend>> {
        return fetchRepository.fetchFriends()
    }
}
