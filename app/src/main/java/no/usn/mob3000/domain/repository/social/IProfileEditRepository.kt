package no.usn.mob3000.domain.repository.social


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
