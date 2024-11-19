package no.usn.mob3000.domain.repository.social

import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.model.social.FriendData

/**
 * Interface for fetching friends and non-friends.
 *
 * @author Husseinabdulameer11
 * @created 2024-11-02
 */
interface IFriendsRepository {
    suspend fun fetchFriends(userId: String): Result<List<FriendData>>
    suspend fun fetchNonFriends(userId: String): Result<List<UserProfile>>
}
