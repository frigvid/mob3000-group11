package no.usn.mob3000.data.repository.auth

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.datetime.Instant
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.model.auth.UserDto
import no.usn.mob3000.data.model.game.GameDataDto
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.domain.model.Friend as DomainFriend
import no.usn.mob3000.domain.model.FriendRequest as DomainFriendRequest
import no.usn.mob3000.domain.model.UserGameStats as DomainUserGameStats
import no.usn.mob3000.domain.model.UserMetadata as DomainUserMetadata
import no.usn.mob3000.domain.model.UserGameStats as DomainUserStats
import no.usn.mob3000.domain.model.UserProfile as DomainUserProfile
import no.usn.mob3000.domain.model.UserSocial as DomainUserSocial
import no.usn.mob3000.domain.model.User as DomainUser
import no.usn.mob3000.domain.repository.IAuthRepository

/**
 * This repository orchestrates authentication-related data operations between
 * the domain layer and the data source. It aggregates data from multiple
 * operations into cohesive results.
 *
 * @property authDataSource The data source for authentication operations.
 * @author frigvid
 * @created 2024-10-22
 */
class AuthRepository(
    private val authDataSource: AuthDataSource,
    private val supabase: SupabaseClient = SupabaseClientWrapper.getClient()
) : IAuthRepository {
    private lateinit var currentUserId: String

    /**
     * Performs user login and fetches associated user data.
     *
     * @param email The user's e-mail address.
     * @param password The user's password.
     * @return A [Result] containing either the complete [UserDto] object on success,
     *         or an error on failure.
     * @author frigvid
     * @created 2024-10-22
     */
    override suspend fun login(
        email: String,
        password: String
    ): Result<DomainUser> {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            currentUserId = authDataSource.getCurrentUserId()

            Result.success(fetchUserData())
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * This is simply a wrapper function for Supabase's authentication function `signOut`.
     *
     * @author frigvid
     * @created 2024-10-28
     */
    override suspend fun logout() = supabase.auth.signOut()

    /**
     * Registration with fetching of userdata.
     *
     * @author Anarox
     * @created 2024-10-30
     */
    override suspend fun register(email: String, password: String): Result<DomainUser> {
        return try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            currentUserId = authDataSource.getCurrentUserId()

            Result.success(fetchUserData())
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    override suspend fun changePassword() {
        TODO("Password change not yet implemented")
    }

    override suspend fun changeEmail() {
        TODO("E-mail change not yet implemented")
    }

    override suspend fun delete() {
        TODO("Account deletion not yet implemented")
    }

    /**
     * Fetches and aggregates all user-related data.
     *
     * @return A [DomainUser] object.
     * @throws Exception if any required data cannot be fetched
     * @author frigvid
     * @created 2024-10-22
     */
    private suspend fun fetchUserData(): DomainUser {
        val user = mapToDomainUser<UserDto>(authDataSource.getCurrentUser())
        Log.d("AuthRepository", "Fetched user: $user")

        val isAdmin = authDataSource.checkAdminStatus(currentUserId)
        Log.d("AuthRepository", "Fetched admin status: $isAdmin")

        val stats = mapToDomainUser<DomainUserGameStats>(authDataSource.getUserGameStats())
        Log.d("AuthRepository", "Fetched user's game stats: $stats")

        val profile = authDataSource.getUserProfile(currentUserId)
            ?.let { mapToDomainUser<DomainUserProfile>(it) }
        Log.d("AuthRepository", "Fetched user profile: $profile")

        val friends = mapToDomainUser<List<DomainFriend>>(authDataSource.getUserFriends(currentUserId))
        Log.d("AuthRepository", "Fetched friends: $friends")

        val friendRequests = mapToDomainUser<List<DomainFriendRequest>>(authDataSource.getUserFriendRequests())
        Log.d("AuthRepository", "Fetched friend requests: $friendRequests")

        return DomainUser(
            id = user.id,
            email = user.email,
            isAdmin = isAdmin,
            stats = stats,
            profile = profile,
            socials = DomainUserSocial(
                friends = friends,
                pendingRequests = friendRequests
            ),
            metadata = DomainUserMetadata(
                createdAt = user.accountCreatedAt,
                updatedAt = user.accountUpdatedAt,
                emailConfirmedAt = user.emailConfirmedAt,
                lastSignInAt = user.lastSignInAt
            )
        )
    }

    /**
     * Generic user DTOs to domain user-objects mapper.
     *
     * @param dto The user DTO to map.
     * @throws IllegalArgumentException if it's not not a user DTO.
     */
    private suspend inline fun <reified T> mapToDomainUser(
        dto: Any
    ): T {
        return when {
            dto is UserInfo && T::class == UserDto::class -> UserDto(
                id = dto.id,
                email = dto.email,
                emailConfirmedAt = dto.emailConfirmedAt,
                invitedAt = dto.invitedAt,
                emailConfirmationSentAt = dto.emailConfirmedAt,
                recoveryEmailSentAt = dto.recoverySentAt,
                emailChangeSentAt = dto.emailChangeSentAt,
                lastSignInAt = dto.lastSignInAt,
                rawUserMetadata = dto.userMetadata,
                accountCreatedAt = dto.createdAt,
                accountUpdatedAt = dto.updatedAt,
                accountBannedUntil = null
            ) as T

            dto is UserDto && T::class == DomainUser::class -> DomainUser(
                id = dto.id,
                email = dto.email,
                isAdmin = dto.isAdmin ?: false,
                stats = null,
                profile = null,
                socials = null,
                metadata = DomainUserMetadata(
                    createdAt = dto.accountCreatedAt ?: Instant.fromEpochMilliseconds(0),
                    updatedAt = dto.accountUpdatedAt ?: Instant.fromEpochMilliseconds(0),
                    emailConfirmedAt = dto.emailConfirmedAt ?: Instant.fromEpochMilliseconds(0),
                    lastSignInAt = dto.lastSignInAt ?: Instant.fromEpochMilliseconds(0)
                )
            ) as T

            dto is GameDataDto && T::class == DomainUserStats::class -> DomainUserStats(
                wins = dto.gameWins ?: 0,
                losses = dto.gameLosses ?: 0,
                draws = dto.gameDraws ?: 0
            ) as T

            dto is ProfileDto && T::class == DomainUserProfile::class -> DomainUserProfile(
                displayName = dto.displayName ?: "",
                avatarUrl = dto.avatarUrl ?: "",
                eloRank = dto.eloRank ?: 0,
                aboutMe = dto.aboutMe ?: "",
                nationality = dto.nationality ?: "",
                visibility = dto.profileVisibility,
                visibilityFriends = dto.friendsVisibility
            ) as T

            dto is List<*> && T::class == List::class -> {
                if (dto.isNotEmpty()) {
                    when (dto[0]) {
                        is FriendsDto -> dto.filterIsInstance<FriendsDto>().map { friendDto ->
                            /* TODO: I'd like to see a better implementation of this, but this
                             *       is good enough for now. This shouldn't be called that often
                             *       anyway. And changes will need to be made later when taking into
                             *       account that background calls should happen periodically to
                             *       update things.
                             */
                            val friendId =
                                if (friendDto.user1 == currentUserId) {
                                    friendDto.user2
                                } else {
                                    friendDto.user1
                                }

                            val friendData = authDataSource.getUserFriendSingle(friendId)

                            DomainFriend(
                                friendshipId = friendData.friendshipId,
                                userId = friendData.friendId,
                                displayName = friendData.displayName ?: "",
                                eloRank = friendData.eloRank ?: 0,
                                avatarUrl = friendData.avatarUrl ?: "",
                                nationality = friendData.nationality ?: ""
                            )
                        } as T

                        is FriendRequestsDto -> dto.filterIsInstance<FriendRequestsDto>().map { requestDto ->
                            DomainFriendRequest(
                                friendRequestId = requestDto.friendRequestId,
                                createdAt = requestDto.createdAt,
                                fromUserId = requestDto.byUser ?: "",
                                toUserId = requestDto.toUser ?: ""
                            )
                        } as T

                        else -> throw IllegalArgumentException("Invalid list type: ${dto[0]!!::class.simpleName}")
                    }
                } else {
                    (emptyList<Any>() as T)
                }
            }

            else -> throw IllegalArgumentException("Invalid mapping: cannot convert ${dto::class.simpleName} to ${T::class.simpleName}")
        }
    }
}
