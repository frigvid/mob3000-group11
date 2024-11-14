package no.usn.mob3000.data.repository.social

import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.repository.social.IUserRepository

/**
 *
 * TODO: Kdoc
 *
 * @author 258030
 * @created 2024-11-09
 */
class UserRepository(
    private val userDataSource: UserDataSource = UserDataSource()
) : IUserRepository {


    override suspend fun getUserProfile(userId: String): Result<UserProfile?> {
        return try {
            val profileDto = userDataSource.getUserProfile(userId)
            Result.success(profileDto?.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }




    private fun ProfileDto.toDomainModel(): UserProfile {
        return UserProfile(
            displayName = this.displayName ?: "",
            avatarUrl = this.avatarUrl ?: "",
            eloRank = this.eloRank ?: 0,
            aboutMe = this.aboutMe ?: "",
            nationality = this.nationality ?: "",
            visibility = this.profileVisibility,
            visibilityFriends = this.friendsVisibility
        )
    }

}
