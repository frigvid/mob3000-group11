package no.usn.mob3000.domain.repository.social


import no.usn.mob3000.domain.model.social.FriendRequestData

interface IFriendRequestRepository {
    suspend fun fetchAllFriendRequests(): Result<List<FriendRequestData>>
    suspend fun insertFriendRequest(
        byUser : String,
        toUser : String,
        accepted : Boolean
    ) : Result<Unit>
    suspend fun deleteFriendRequest(userid: String): Result<Unit>
}
