package no.usn.mob3000.domain.usecase.social.FriendRequest

import no.usn.mob3000.data.repository.social.FriendRequestRepository

class AcceptFriendRequestUseCase(
    private val friendRequestRepository: FriendRequestRepository = FriendRequestRepository()
) {
    suspend fun accept(friendRequestId: String): Result<Unit> {
        return friendRequestRepository.acceptFriendRequest(friendRequestId)
    }

    suspend fun decline(friendRequestId: String): Result<Unit> {
        return friendRequestRepository.declineFriendRequest(friendRequestId)
    }
}
