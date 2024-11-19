package no.usn.mob3000.domain.repository.social

/**
 * Interface for the profile edit repository.
 *
 * @author 258030
 * @created 2024-11-15
 */
interface IProfileEditRepository {
    suspend fun updateProfile(
        userid: String,
        displayName: String,
        avatarUrl: String,
        aboutMe: String,
        nationality: String,
        profileVisibility: Boolean,
        friendsVisibility: Boolean
    ): Result<Unit>
}
