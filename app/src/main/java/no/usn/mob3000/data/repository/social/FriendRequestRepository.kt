package no.usn.mob3000.data.repository.social

import android.util.Log
import kotlinx.datetime.Clock
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.social.FriendRequestDataSource
import no.usn.mob3000.data.source.remote.social.ProfileUserDataSource
import no.usn.mob3000.domain.model.social.FriendRequestData
import no.usn.mob3000.domain.repository.social.IFriendRequestRepository
import java.util.UUID

/**
 * 
 * This repository orchestrates friend request-related data operations. Shares similarities with [FriendsRepository]. In future versions, we would
 * ideally want to abstract this across all database operations.
 *
 * The class contains functionality for fetching, inserting, updating, and handling the requests (accept/decline). Implemented mapping with [toDomainModel],
 * joining [displayName] from [UserProfile] with [FriendRequestData] to associate the common userId between the tables.
 *
 * @param authDataSource The data source for authentication.
 * @param friendRequestDataSource The data source for friend-requests.
 * @param profileUserDataSource The data source for user profile.
 * @author 258030
 * @contributor Husseinabdulameer11
 * @created 2024-11-16
 */
class FriendRequestRepository (
    private val authDataSource: AuthDataSource = AuthDataSource(),
    private val friendRequestDataSource: FriendRequestDataSource = FriendRequestDataSource(),
    private val profileUserDataSource: ProfileUserDataSource = ProfileUserDataSource()
) : IFriendRequestRepository {
    /**
     * Fetches all friend requests. Ownership of the list (current logged in entity) is declared
     * in the domain/ui layer.
     *
     * TODO: Remove logs
     *
     * @return [Result] containing a list of [FriendRequestData] if successful, otherwise an error.
     * @throws Exception if an error occurs during the fetch.
     * @author 258030
     * @contributor Husseinabdulameer11
     * @created 2024-11-16
     */
    override suspend fun fetchFriendRequests(): Result<List<FriendRequestData>> {
        return try {
            val currentUserId = authDataSource.getCurrentUserId()
            val requestsList = friendRequestDataSource.fetchFriendRequests(currentUserId)
            requestsList.forEach { friend ->
                Log.d("FriendRequestRepository", "Friend: $friend")
            }
            Result.success(requestsList.map { it.toDomainModel() })
        } catch (e: Exception) {
            Log.e("FriendRequestRepository", "Failed to fetch friend requests: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     *
     * Inserts a new friend request after interacting with [ProfileAddFriendsScreen].
     *
     * @param toUser The user ID to whom the friend request is being sent.
     * @return [Result] indicating success or failure.
     * @author Husseinabdulameer11
     * @created 2024-11-16
     */
    override suspend fun insertFriendRequest(toUser: String): Result<Unit>{
        val requestItem = FriendRequestsDto(
            friendRequestId = UUID.randomUUID().toString(),
            createdAt = Clock.System.now(),
            byUser = authDataSource.getCurrentUserId(),
            toUser = toUser,
            accepted = null
        )
        return friendRequestDataSource.insertFriendRequest(requestItem)
    }

    /**
     *
     * Accepts a friend request by its ID.
     *
     * This function inserts the friend pair into the friends table and deletes the original friend request.
     *
     * @param friendRequestId The ID of the friend request to be accepted.
     * @return [Result] indicating success or failure.
     * @author 258030
     * @created 2024-11-16
     */
    override suspend fun acceptFriendRequest(friendRequestId: String): Result<Unit> {
        return try {
            val friendRequest = friendRequestDataSource.fetchFriendRequestById(friendRequestId)
            if (friendRequest != null) {
                val newFriend = FriendsDto(
                    friendshipId = UUID.randomUUID().toString(),
                    friendsSince = Clock.System.now(),
                    user1 = friendRequest.byUser ?: "",
                    user2 = friendRequest.toUser ?: ""
                )
                friendRequestDataSource.insertFriend(newFriend)
                deleteFriendRequest(friendRequestId)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Friend request $friendRequestId does not exist"))
            }
        } catch (e: Exception) {
            Log.e("FriendRequestRepository", "Error after accepting: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     *
     * Declines a friend request by its ID.
     * This function simply deletes the friend request without any further action.
     *
     * TODO: Might add a confirmation option here?
     *
     * @param friendRequestId The ID of the friend request to be declined.
     * @return [Result] indicating success or failure.
     * @author 258030
     * @created 2024-11-16
     */
    override suspend fun declineFriendRequest(friendRequestId: String): Result<Unit> {
        return deleteFriendRequest(friendRequestId)
    }

    /**
     *
     * Declines a friend requests after interaction with [ProfileFriendRequestsScreen]. This function is used by both the accept and decline option, since
     * the friend request is deleted in both cases.
     *
     * @param friendRequestId The ID of the friend request to be deleted.
     * @return [Result] indicating success or failure.
     * @author 258030
     * @contributor Husseinabdulameer11
     * @created 2024-11-16
     */
    private suspend fun deleteFriendRequest(friendRequestId: String): Result<Unit> {
        return try {
            friendRequestDataSource.deleteFriendRequestById(friendRequestId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FriendRequestRepository", "Failed to delete friend request: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     *
     * Converts [FriendRequestsDto] to [FriendRequestData]. "accepted" is not really used in the implementation.
     *
     * @return The corresponding [FriendRequestData] instance.
     */
    private fun FriendRequestsDto.toDomainModel(): FriendRequestData {
        return FriendRequestData(
            friendRequestId = this.friendRequestId,
            accepted = this.accepted ?: false,
            toUser = this.toUser ?: "",
            byUser = this.byUser ?: "",
            createdAt = this.createdAt ?: Clock.System.now()
        )
    }
}
