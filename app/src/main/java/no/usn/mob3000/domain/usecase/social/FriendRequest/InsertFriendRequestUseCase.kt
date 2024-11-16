package no.usn.mob3000.domain.usecase.social.FriendRequest

import no.usn.mob3000.data.repository.social.FriendRequestRepository

class InsertFriendRequestUseCase(
    private val friendRequestRepository: FriendRequestRepository = FriendRequestRepository()
){
    suspend fun execute(toUser: String):Result<Unit>{
        return friendRequestRepository.insertFriendRequest(toUser)
    }
}
