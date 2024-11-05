package no.usn.mob3000.domain.usecase.social

/**
 * @author Hussein Abdul-Ameer
 * created on 03.11.2024
 */
import no.usn.mob3000.data.repository.social.FriendsRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.domain.model.social.FriendData

class FetchFriendsUseCase (
    private val fetchrepository: FriendsRepository = FriendsRepository()
){
    suspend fun fetchFriends():Result<List<FriendData>> {
        return fetchrepository.FetchFriends()
    }
}
