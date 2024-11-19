package no.usn.mob3000.domain.usecase.social

import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.data.repository.social.UserRepository

/**
 * This use case retrieves the user's profile information.
 *
 * @param userRepository The repository for user operations.
 * @author Husseinabdulameer11
 * @created: 2024-11-10
 */
class FetchUserProfileUseCase(
    private val userRepository: UserRepository
) {
    /**
     * Gets the users user profile.
     *
     * @param userId The user's UUID.
     */
    suspend operator fun invoke(userId: String): Result<UserProfile?> {
        return userRepository.getUserProfile(userId)
    }
}
