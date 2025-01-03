package no.usn.mob3000.data.repository.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.datetime.Instant
import no.usn.mob3000.data.model.auth.UserDto
import no.usn.mob3000.data.model.game.GameDataDto
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.repository.IAuthRepository
import no.usn.mob3000.domain.model.auth.Friend as DomainFriend
import no.usn.mob3000.domain.model.auth.FriendRequest as DomainFriendRequest
import no.usn.mob3000.domain.model.auth.User as DomainUser
import no.usn.mob3000.domain.model.auth.UserGameStats as DomainUserGameStats
import no.usn.mob3000.domain.model.auth.UserGameStats as DomainUserStats
import no.usn.mob3000.domain.model.auth.UserMetadata as DomainUserMetadata
import no.usn.mob3000.domain.model.auth.UserProfile as DomainUserProfile
import no.usn.mob3000.domain.model.auth.UserSocial as DomainUserSocial

/**
 * This repository orchestrates authentication-related data operations between
 * the domain layer and the data source. It aggregates data from multiple
 * operations into cohesive results.
 *
 * @property authDataSource The data source for authentication operations.
 * @property userDataSource The data source for user operations.
 * @property supabase The Supabase client.
 * @author frigvid
 * @created 2024-10-22
 */
class AuthRepository(
    private val authDataSource: AuthDataSource,
    val userDataSource: UserDataSource,
    private val supabase: SupabaseClient = SupabaseClientWrapper.getClient()
) : IAuthRepository {
    lateinit var currentUserId: String

    /**
     * Gets the currently authenticated user's id.
     *
     * @author Husseinabdulameer11
     * @created 2024-11-18
     */
    override suspend fun getCurrentUserId(): String = authDataSource.getCurrentUserId()

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
     * Performs user registration.
     *
     * @author Anarox1111
     * @contributor frigvid
     * @created 2024-10-30
     */
    override suspend fun register(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }

            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Updates the currently authenticated user's account with a new password.
     *
     * ## References
     *
     * [Supabase-kt 'update a user' docs](https://supabase.com/docs/reference/kotlin/auth-updateuser).
     *
     * @param newPassword The password to change to.
     * @author frigvid
     * @created 2024-11-13
     */
    override suspend fun changePassword(
        newPassword: String
    ): Result<Unit> {
        return try {
            supabase.auth.updateUser {
                password = newPassword
            }

            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Request a forgotten password e-mail.
     *
     * ## References
     *
     * [Supabase-kt 'send a password reset request' docs](https://supabase.com/docs/reference/kotlin/auth-resetpasswordforemail).
     *
     * @param email The e-mail address of the user requesting the forgotten password e-mail.
     * @author frigvid
     * @created 2024-11-13
     */
    override suspend fun forgotPassword(
        email: String
    ): Result<Unit> {
        return try {
            supabase.auth.resetPasswordForEmail(email)

            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Updates the currently authenticated user's account with a new e-mail.
     *
     * ## References
     *
     * [Supabase-kt 'update a user' docs](https://supabase.com/docs/reference/kotlin/auth-updateuser).
     *
     * @param newEmail The e-mail address to change to.
     * @author frigvid
     * @created 2024-11-13
     */
    override suspend fun changeEmail(
        newEmail: String
    ): Result<Unit> {
        return try {
            supabase.auth.updateUser {
                email = newEmail
            }

            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Deletes the authenticated user via an RPC function.
     *
     * ## References
     *
     * Web application's SQL [pre-requisites](https://github.com/frigvid/app2000-gruppe11/blob/master/PREREQUISITES.sql#L868) on line 868.
     *
     * @author frigvid
     * @created 2024-11-03
     */
    override suspend fun delete() {
        try {
            val result = supabase.postgrest.rpc(function = "user_delete")
            Logger.d("RPC function user_delete result: ${result.data}")
        } catch (error: Exception) {
            throw Exception("Failed to delete authenticated user's account: ${error.message}", error)
        }
    }

    /**
     * Imports a authentication session token into the Supabase client.
     *
     * ## Note
     *
     * Useful for deep linking.
     *
     * @author Anarox
     * @created 2024-11-18
     */
    override suspend fun importSessionToken(
        sessionToken: String
    ) {
        try {
            supabase.auth.importAuthToken(sessionToken)
        } catch (error: Exception) {
            throw Exception("Failed to import session token! ${error.message}", error)
        }
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
        val user = mapToDomainUser<UserDto>(userDataSource.getCurrentUser())
        Logger.d("Fetched user: $user")

        val isAdmin = authDataSource.checkAdminStatus()
        Logger.d("Fetched admin status: $isAdmin")

        val stats = mapToDomainUser<DomainUserGameStats>(userDataSource.getUserGameStats())
        Logger.d("Fetched user's game stats: $stats")

        val profile = userDataSource.getUserProfile(currentUserId)
            ?.let { mapToDomainUser<DomainUserProfile>(it) }
        Logger.d("Fetched user profile: $profile")

        val friends = mapToDomainUser<List<DomainFriend>>(userDataSource.getUserFriends(currentUserId))
        Logger.d("Fetched friends: $friends")

        val friendRequests = mapToDomainUser<List<DomainFriendRequest>>(userDataSource.getUserFriendRequests())
        Logger.d("Fetched friend requests: $friendRequests")

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
     * @author frigvid
     * @created 2024-10-22
     */
    suspend inline fun <reified T> mapToDomainUser(
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
                userId = dto.userId,
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

                            val friendData = userDataSource.getUserFriendSingle(friendId)

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
                                createdAt = requestDto.createdAt ?: Instant.fromEpochMilliseconds(0),
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
