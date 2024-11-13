package no.usn.mob3000.data.repository.social

import kotlinx.datetime.Instant
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.social.UserDataSource
import no.usn.mob3000.domain.repository.social.IProfileEditRepository

class ProfileEditRepository (
    private val authDataSource: AuthDataSource,
    private val profileEditDataSource: UserDataSource
):IProfileEditRepository{

    override suspend fun updateProfile(
        userid: String,
        updatedAt: Instant,
        displayName: String,
        avatarUrl: String,
        aboutMe: String,
        nationality: String,
        profileVisibility: Boolean,
        friendsVisibility: Boolean
    ) : Result<Unit>{
        val originalUser = profileEditDataSource.fetchUserById(userid)
        return if(originalUser != null){
            val updateProfileDto = ProfileDto(
                userId = userid,
                    updatedAt = originalUser.updatedAt,
                    displayName = displayName,
                    avatarUrl = avatarUrl,
                    aboutMe = avatarUrl,
                    nationality = nationality,
                    profileVisibility = profileVisibility,
                    friendsVisibility = friendsVisibility
            )
            profileEditDataSource.updateProfile(userid,updateProfileDto)
        }else{
            Result.failure(Exception("Original User data not found"))
        }
    }
}
