package no.usn.mob3000.domain.repository.social

import kotlinx.datetime.Instant
import no.usn.mob3000.domain.model.social.FriendData

interface IProfileEditRepository {
    suspend fun updateProfile(
        userid: String,
        updatedAt: Instant,
        displayName: String,
        avatarUrl: String,
        aboutMe: String,
        nationality: String,
        profileVisibility: Boolean,
        friendsVisibility: Boolean

    ): Result<Unit>


}
