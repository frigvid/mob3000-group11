package no.usn.mob3000.domain.usecase.social.userUpdate

import no.usn.mob3000.domain.repository.social.IProfileEditRepository

/**
 * Usercase for updating profile
 *
 * @param profileEditRepository The repository for updating profile
 * @author Husseinabdulameer11
 * @created: 2024-11-10
 */
class UpdateProfileUseCase(
    private val profileEditRepository: IProfileEditRepository
) {
    suspend fun execute(
        userid: String,
        displayName: String,
        avatarUrl: String,
        aboutMe: String,
        nationality: String,
        profileVisibility: Boolean,
        friendsVisibility: Boolean
    ): Result<Unit> {
        return profileEditRepository.updateProfile(userid,displayName,avatarUrl,aboutMe,nationality,profileVisibility,friendsVisibility)
    }
}
