package no.usn.mob3000.domain.usecase.social.Profile

import no.usn.mob3000.data.repository.social.FriendsRepository
import no.usn.mob3000.domain.model.social.FriendData

/**
 * @author Husseinabdulameer11
 * @created : 03.11.2024
 */

class FetchFriendsUseCase (
    private val fetchrepository: FriendsRepository = FriendsRepository()
){
    suspend fun fetchFriends():Result<List<FriendData>> {
        return fetchrepository.fetchFriends()
    }
}
