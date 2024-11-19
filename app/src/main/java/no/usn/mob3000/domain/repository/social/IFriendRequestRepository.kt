package no.usn.mob3000.domain.repository.social

import no.usn.mob3000.domain.model.social.FriendRequestData

/**
 * Interface for the friend request repository.
 *
 * @author 258030
 * @created 2024-11-16
 */
interface IFriendRequestRepository {
    suspend fun fetchFriendRequests(): Result<List<FriendRequestData>>
    suspend fun insertFriendRequest(toUser: String): Result<Unit>
    suspend fun acceptFriendRequest(friendRequestId: String): Result<Unit>
    suspend fun declineFriendRequest(friendRequestId: String): Result<Unit>
}
