package no.usn.mob3000.data.repository.social

import no.usn.mob3000.data.model.game.GameDataDto
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.domain.model.auth.UserGameStats
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.repository.social.IUserRepository
/**
 * This repository is responsible for generating information about the current user. It uses generic auth
 * methods from [UserDataSource]. [fetchUserById] exist in another from in [AuthRepository], but our usecase demands some changes
 * to the notation, so instead of refactoring something thats beeing used for a lot of other things, it has its own
 * function here
 *
 * @param userDataSource The data source for user operations.
 * @author 258030
 * @created 2024-11-09
 */
class UserRepository(
    private val userDataSource: UserDataSource = UserDataSource()
) : IUserRepository {
    /**
     * Retrieves the user's profile information.
     *
     * @param userId The ID of the user.
     */
    override suspend fun getUserProfile(userId: String): Result<UserProfile?> {
        return try {
            val profileDto = userDataSource.getUserProfile(userId)
            Result.success(profileDto?.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    /**
     *  Retrieves the specified user's Id.
     *
     *  @param userId The ID of the user.
     */
    override suspend fun fetchUserById(userId: String): Result<UserProfile?> {
        return try {
            val profileDto = userDataSource.fetchUserById(userId)
            Result.success(profileDto?.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    /**
     * Retrieves the user's game statistics.
     */
    override suspend fun getUserGameStats(): Result<UserGameStats> {
        return try {
            val gameDataDto = userDataSource.getUserGameStats()
            Result.success(gameDataDto.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     *  Converts a [ProfileDto] to a [UserProfile]
     */
    private fun ProfileDto.toDomainModel(): UserProfile {
        return UserProfile(
            userId = this.userId,
            displayName = this.displayName ?: "",
            avatarUrl = this.avatarUrl ?: "",
            eloRank = this.eloRank ?: 0,
            aboutMe = this.aboutMe ?: "",
            nationality = this.nationality ?: "",
            visibility = this.profileVisibility,
            visibilityFriends = this.friendsVisibility
        )
    }
    private fun GameDataDto.toDomainModel(): UserGameStats {
        return UserGameStats(
            wins = this.gameWins ?: 0,
            losses = this.gameLosses ?: 0,
            draws = this.gameDraws ?: 0
        )
    }
}
