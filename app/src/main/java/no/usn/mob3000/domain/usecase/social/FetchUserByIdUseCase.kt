package no.usn.mob3000.domain.usecase.social

import no.usn.mob3000.data.repository.social.UserRepository
import no.usn.mob3000.domain.model.auth.UserProfile

/**
 * This use case retrieves the user's profile information.
 *
 * @param userRepository The repository for user operations.
 * @author Husseinabdulameer11
 * @created 2024-11-10
 */
class FetchUserByIdUseCase(
    private val userRepository: UserRepository = UserRepository()
) {
    /**
     * Get a user by the ID.
     *
     * @param userId The user's UUID.
     */
    suspend operator fun invoke(userId: String): Result<UserProfile?> {
        return userRepository.fetchUserById(userId)
    }
}
