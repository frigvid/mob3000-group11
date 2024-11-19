package no.usn.mob3000.domain.usecase.social

import no.usn.mob3000.data.repository.social.FriendRequestRepository
import no.usn.mob3000.data.repository.social.FriendsRepository
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.model.social.FriendData
import no.usn.mob3000.domain.model.social.FriendRequestData

/**
 * Fetch.case for friends
 *
 * @author 258030
 * @contributor Husseinabdulameer11
 * @created 2024-11-03
 */
class FetchFriendsUseCase (
    private val fetchRepository: FriendsRepository = FriendsRepository(),
    private val fetchRequestRepository: FriendRequestRepository = FriendRequestRepository()
) {
    /**
     * Get the friends of the user.
     *
     * @param userId The user's UUID.
     */
    suspend fun fetchFriends(userId: String): Result<List<FriendData>> {
        return fetchRepository.fetchFriends(userId)
    }

    /**
     * Get any users who are not friends with the user.
     *
     * @param userId The user's UUID.
     */
    suspend fun fetchNonFriends(userId: String): Result<List<UserProfile>> {
        return fetchRepository.fetchNonFriends(userId)
    }

    /**
     * Get the user's friend requests.
     */
    suspend fun fetchFriendRequests(): Result<List<FriendRequestData>>{
        return fetchRequestRepository.fetchFriendRequests()
    }
}
