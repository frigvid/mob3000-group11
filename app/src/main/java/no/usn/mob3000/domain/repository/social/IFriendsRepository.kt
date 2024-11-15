

/**
 * @author Husseinabdulameer11
 * @created : 02.11.2024
 */
// In IFriendsRepository
// In IFriendsRepository

package no.usn.mob3000.domain.repository.social

import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.model.social.FriendData

interface IFriendsRepository {
    suspend fun FetchFriends(): Result<List<FriendData>>
    suspend fun getFriendProfile(userId: String): Result<List<FriendData>>
    suspend fun fetchNonFriends(userId: String): Result<List<UserProfile>>
}
