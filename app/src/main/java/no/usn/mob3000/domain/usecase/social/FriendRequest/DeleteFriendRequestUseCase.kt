package no.usn.mob3000.domain.usecase.social.FriendRequest

import no.usn.mob3000.data.repository.social.FriendRequestRepository
import no.usn.mob3000.domain.repository.social.IFriendRequestRepository

class DeleteFriendRequestUseCase (
    private val friendRequestRepository: IFriendRequestRepository = FriendRequestRepository()
){
    suspend fun deleteFriendRequest(userId:String): Result<Unit>{
        return friendRequestRepository.deleteFriendRequest(userId)
    }
}

