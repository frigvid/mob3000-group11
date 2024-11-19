package no.usn.mob3000.domain.usecase.social

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
    /**
     * Accept a friend request.
     *
     * @param friendRequestId The friend request UUID.
     */
    suspend fun accept(friendRequestId: String): Result<Unit> {
        return friendRequestRepository.acceptFriendRequest(friendRequestId)
    }

    /**
     * Decline a friend request.
     *
     * @param friendRequestId The friend request UUID.
     */
    suspend fun decline(friendRequestId: String): Result<Unit> {
        return friendRequestRepository.declineFriendRequest(friendRequestId)
    }

    /**
     * Add a friend request.
     *
     * @param toUser A users UUID.
     */
    suspend fun insert(toUser: String): Result<Unit> {
        return friendRequestRepository.insertFriendRequest(toUser)
    }
}

