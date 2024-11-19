package no.usn.mob3000.data.repository.social

import kotlinx.datetime.Clock
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.data.source.remote.social.ProfileUserDataSource
import no.usn.mob3000.domain.repository.social.IProfileEditRepository
/**
 *
 * This repository orchestrates the updating of the users profile. Currently there is only one implementation of
 * updating a table under profile, thus [updateProfile] being the sole function here
 *
 * @param profileEditDataSource The data source for fetching friends.
 * @author 258030
 * @created 2024-11-15
 */
class ProfileEditRepository (
    private val profileEditDataSource: ProfileUserDataSource = ProfileUserDataSource(),
    private val userDataSource: UserDataSource = UserDataSource()
): IProfileEditRepository{
    /**
     *  Updates the users profile. As long as the value of the user exist, we try to update. Else throw exception
     *
     *  @param userid The user id of the current user.
     *  @param displayName The display name of the user.
     *  @param avatarUrl The avatar url of the user.
     *  @param aboutMe The about me of the user.
     *  @param nationality The nationality of the user.
     *  @param profileVisibility The profile visibility of the user.
     *  @param friendsVisibility The friends visibility of the user.
     */
    override suspend fun updateProfile(
        userid: String,
        displayName: String,
        avatarUrl: String,
        aboutMe: String,
        nationality: String,
        profileVisibility: Boolean,
        friendsVisibility: Boolean
    ) : Result<Unit>{
        val originalUser = userDataSource.fetchUserById(userid)
        return if( originalUser != null ) {
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
        } else{
            Result.failure(Exception("Original User data not found"))
        }
    }
}
