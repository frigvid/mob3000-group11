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

class FriendRequestRepository (
    private val authDataSource: AuthDataSource = AuthDataSource(),
    private val friendRequestDataSource: FriendRequestDataSource = FriendRequestDataSource(),
    private val profileUserDataSource: ProfileUserDataSource = ProfileUserDataSource()
) : IFriendRequestRepository {


    override suspend fun fetchAllFriendRequests(): Result<List<FriendRequestData>> {
        return try {
            val currentUserId = authDataSource.getCurrentUserId()
            val friendRequestsDtoList = friendRequestDataSource.fetchAllFriendRequests(currentUserId)

            val userIds = friendRequestsDtoList.mapNotNull { it.byUser }.distinct()

            val userProfiles = userIds.mapNotNull { userId ->
                try {
                    profileUserDataSource.fetchUserById(userId)
                } catch (e: Exception) {
                    Log.e("FriendRequestRepository", "Failed to fetch user profile for ID: $userId")
                    null
                }
            }.associateBy { it.userId }
            val friendRequests = friendRequestsDtoList.map { request ->
                val displayName = userProfiles[request.byUser]?.displayName ?: ""
                request.toDomainModel(displayName)
            }
            Log.d("FriendRequestRepository", "Fetched friend requests: $friendRequests")
            Result.success(friendRequests)
        } catch (e: Exception) {
            Log.e("FriendRequestRepository", "Failed to fetch friend requests: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun insertFriendRequest(toUser: String): Result<Unit>{
        val friendRequestItem = FriendRequestsDto(
            friendRequestId = UUID.randomUUID().toString(),
            createdAt = Clock.System.now(),
            byUser = authDataSource.getCurrentUserId(),
            toUser = toUser,
            accepted = null
        )
        return friendRequestDataSource.insertFriendRequest(friendRequestItem)

    }
    override suspend fun deleteFriendRequest(friendRequestId: String): Result<Unit> {
        return try {
            friendRequestDataSource.deleteFriendRequestById(friendRequestId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FriendRequestRepository", "Failed to delete friend request: ${e.message}")
            Result.failure(e)
        }
    }

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

    override suspend fun declineFriendRequest(friendRequestId: String): Result<Unit> {
        return deleteFriendRequest(friendRequestId)
    }



    private fun FriendRequestsDto.toDomainModel(displayName: String): FriendRequestData {
        return FriendRequestData(
            friendRequestId = this.friendRequestId,
            accepted = this.accepted ?: false,
            toUser = this.toUser ?: "",
            byUser = this.byUser ?: "",
            createdAt = this.createdAt?: Clock.System.now(),
            displayName = displayName
        )
    }
}
