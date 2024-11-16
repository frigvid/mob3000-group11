package no.usn.mob3000.domain.usecase.social.requests

import no.usn.mob3000.data.repository.social.FriendRequestRepository
/**
 * Use cases for friend request
 *
 * @param friendRequestRepository The repository for friend request
 * @author 258030
 * @created: 2024-11-15
 */
class FriendRequestUseCase(
    private val friendRequestRepository: FriendRequestRepository = FriendRequestRepository()
) {
    suspend fun accept(friendRequestId: String): Result<Unit> {
        return friendRequestRepository.acceptFriendRequest(friendRequestId)
    }
    suspend fun decline(friendRequestId: String): Result<Unit> {
        return friendRequestRepository.declineFriendRequest(friendRequestId)
    }
    suspend fun insert(toUser: String): Result<Unit> {
        return friendRequestRepository.insertFriendRequest(toUser)
    }
}

