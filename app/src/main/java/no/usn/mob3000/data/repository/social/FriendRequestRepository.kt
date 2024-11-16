package no.usn.mob3000.data.repository.social

import kotlinx.datetime.Clock
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.social.FriendRequestDataSource
import no.usn.mob3000.domain.model.social.FriendRequestData
import no.usn.mob3000.domain.repository.social.IFriendRequestRepository
import java.util.UUID

class FriendRequestRepository (
    private val authDataSource: AuthDataSource = AuthDataSource(),
    private val friendRequestDataSource: FriendRequestDataSource = FriendRequestDataSource()
) : IFriendRequestRepository {
    override suspend fun fetchAllFriendRequests(): Result<List<FriendRequestData>>{
        return try {
            val friendRequestsDtoList = friendRequestDataSource.fetchAllFriendRequests()
            Result.success(friendRequestsDtoList.map{it.toDomainModel()})
        } catch (e: Exception) {
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
    override suspend fun deleteFriendRequest(userId: String): Result<Unit> {
        return try {
            friendRequestDataSource.deleteFriendRequestById(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun FriendRequestsDto.toDomainModel(): FriendRequestData {
        return FriendRequestData(

            accepted = this.accepted ?: false,
            toUser = this.toUser ?: "",
            byUser = this.byUser ?: "",
            createdAt = this.createdAt?: Clock.System.now() ,
            friendRequestId = this.friendRequestId
        )
    }
}
