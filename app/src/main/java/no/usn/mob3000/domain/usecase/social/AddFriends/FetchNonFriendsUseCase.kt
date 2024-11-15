package no.usn.mob3000.domain.usecase.social.AddFriends

import no.usn.mob3000.data.repository.social.FriendsRepository
import no.usn.mob3000.domain.model.auth.UserProfile

class FetchNonFriendsUseCase(
    private val friendsRepository: FriendsRepository = FriendsRepository()
) {
    suspend fun execute(userId: String): Result<List<UserProfile>> {
        return friendsRepository.fetchNonFriends(userId)
    }
}
