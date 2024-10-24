package no.usn.mob3000.data.repository

import android.util.Log
import no.usn.mob3000.data.source.remote.AuthDataSource
import no.usn.mob3000.data.model.UserDto
import no.usn.mob3000.data.model.UserSocialDto

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
    private val authDataSource: AuthDataSource
) {
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
    suspend fun login(
        email: String,
        password: String
    ): Result<UserDto> {
        return try {
            authDataSource.signIn(email, password)

            val id = authDataSource.getCurrentUserId() ?: throw IllegalStateException("User not logged in")
            val user = fetchUserData(id)

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fetches and aggregates all user-related data.
     *
     * TODO: Make this a little more robust. Not all data can be fetched relative to other user's
     *       visibility, friend list visibility or RLS.
     *
     * @param userId The user's `auth.users.id` UUID.
     * @return A [UserDto] object.
     * @throws Exception if any required data cannot be fetched
     * @author frigvid
     * @created 2024-10-22
     */
    private suspend fun fetchUserData(
        userId: String
    ): UserDto {
        val user = authDataSource.fetchUser()
        Log.d("AuthRepository", "Fetched user: $user")

        val isAdmin = authDataSource.checkAdminStatus(userId)
        Log.d("AuthRepository", "Fetched admin status: $isAdmin")

        val profile = authDataSource.fetchUserProfile(userId)
        Log.d("AuthRepository", "Fetched user profile: $profile")

        val friends = authDataSource.fetchFriends()
        Log.d("AuthRepository", "Fetched friends: $friends")

        val friendRequests = authDataSource.fetchFriendRequests()
        Log.d("AuthRepository", "Fetched friend requests: $friendRequests")

        return UserDto(
            id = user.id,
            email = user.email,
            isAdmin = isAdmin,
            accountCreatedAt = user.createdAt,
            accountUpdatedAt = user.updatedAt,
            emailConfirmedAt = user.emailConfirmedAt,
            emailConfirmationSentAt = user.confirmationSentAt,
            recoveryEmailSentAt = user.recoverySentAt,
            lastSignInAt = user.lastSignInAt,
            profile = profile,
            socialData = UserSocialDto(friends, friendRequests)
        )
    }
}