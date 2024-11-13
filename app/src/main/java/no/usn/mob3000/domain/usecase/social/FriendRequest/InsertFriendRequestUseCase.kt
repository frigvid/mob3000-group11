package no.usn.mob3000.domain.usecase.social.FriendRequest

import no.usn.mob3000.data.repository.social.FriendRequestRepository

class InsertFriendRequestUseCase(
    private val friendRequestRepository: FriendRequestRepository
){
    suspend fun execute(
        byUser: String,
        toUser: String,
        accepted: Boolean
    ):Result<Unit>{
        return friendRequestRepository.insertFriendRequest(byUser,toUser,accepted)
    }
}
