package no.usn.mob3000.domain.usecase.social.ProfileEdit


import kotlinx.datetime.Instant
import no.usn.mob3000.domain.repository.social.IProfileEditRepository

class UpdateProfileUseCase(
    private val profileEditRepository: IProfileEditRepository
) {
    suspend fun execute(
        userid: String,
        updatedAt: Instant,
        displayName: String,
        avatarUrl: String,
        aboutMe: String,
        nationality: String,
        profileVisibility: Boolean,
        friendsVisibility: Boolean

    ): Result<Unit> {
        return profileEditRepository.updateProfile(userid,updatedAt,displayName,avatarUrl,aboutMe,nationality,profileVisibility,friendsVisibility)
    }
}
