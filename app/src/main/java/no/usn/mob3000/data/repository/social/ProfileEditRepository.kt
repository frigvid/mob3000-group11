package no.usn.mob3000.data.repository.social

import kotlinx.datetime.Clock
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.source.remote.social.ProfileUserDataSource
import no.usn.mob3000.domain.repository.social.IProfileEditRepository

class ProfileEditRepository (
    private val profileEditDataSource: ProfileUserDataSource = ProfileUserDataSource()
):IProfileEditRepository{

    override suspend fun updateProfile(
        userid: String,
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
                    updatedAt = Clock.System.now(),
                    displayName = displayName,
                    avatarUrl = avatarUrl,
                    aboutMe = aboutMe,
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
