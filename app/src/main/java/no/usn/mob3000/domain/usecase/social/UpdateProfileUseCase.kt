package no.usn.mob3000.domain.usecase.social

import no.usn.mob3000.domain.repository.social.IProfileEditRepository

/**
 * Use case for updating profile
 *
 * @param profileEditRepository The repository for updating profile
 * @author Husseinabdulameer11
 * @created 2024-11-10
 */
class UpdateProfileUseCase(
    private val profileEditRepository: IProfileEditRepository
) {
    /**
     * Update the user's profile.
     *
     * @param userId The user's UUID.
     * @param displayName The user's display name.
     * @param avatarUrl The user's profile picture URL.
     * @param aboutMe The user's about me section.
     * @param nationality The user's nationality.
     * @param profileVisibility The user's profile visibility. With this set to false, the profile
     *                          is not traversable or discoverable by other users.
     * @param friendsVisibility The user's profile's friends list visibility. With this set to false,
     *                          the profile is traversable and discoverable by other users, but the
     *                          friends list is hidden.
     */
    suspend fun execute(
        userId: String,
        displayName: String,
        avatarUrl: String,
        aboutMe: String,
        nationality: String,
        profileVisibility: Boolean,
        friendsVisibility: Boolean
    ): Result<Unit> {
        return profileEditRepository.updateProfile(
            userId,
            displayName,
            avatarUrl,
            aboutMe,
            nationality,
            profileVisibility,
            friendsVisibility
        )
    }
}
