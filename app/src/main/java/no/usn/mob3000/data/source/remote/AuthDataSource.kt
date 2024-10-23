package no.usn.mob3000.data.source.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.gotrue.user.UserInfo
import no.usn.mob3000.data.SupabaseClientWrapper
import no.usn.mob3000.data.model.UserFriendRequestsDto
import no.usn.mob3000.data.model.UserFriendsDto
import no.usn.mob3000.data.model.UserProfileDto

/**
 * The data source for authentication-related operations.
 *
 * @property supabase The Supabase client.
 * @author frigvid
 * @created 2024-10-22
 */
class AuthDataSource(
    private val supabase: SupabaseClient = SupabaseClientWrapper.getClient()
) {
    /**
     * Log the user in.
     *
     * TODO: Improve handling.
     *
     * @param email The user's e-mail address.
     * @param password The user's password.
     * @throws Exception if authentication fails.
     * @author frigvid
     * @created 2024-10-22
     */
    suspend fun signIn(email: String, password: String) {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    /**
     * Gets the full user object.
     *
     * TODO: Consider replacing [[getCurrentUserId]] with this in the `AuthRepository`.
     *
     * @return The user's [UserInfo] object.
     * @throws Exception if the user cannot be fetched or decoded.
     * @author frigvid
     * @created 2024-10-22
     */
    suspend fun fetchUser(): UserInfo {
        return supabase.auth.retrieveUserForCurrentSession(updateSession = true)
    }

    /**
     * Get the user's profile.
     *
     * @param userId The user's `auth.users.id` UUID.
     * @return The user's [UserProfileDto] data.
     * @throws Exception if the profile cannot be fetched or decoded.
     * @author frigvid
     * @created 2024-10-22
     */
    suspend fun fetchUserProfile(userId: String): UserProfileDto {
        return try {
            supabase.postgrest.rpc(
                    function = "profile_get",
                    parameters = mapOf("usr_id" to userId)
                ).decodeSingleOrNull<UserProfileDto>()
                ?: throw NoSuchElementException("No profile found for user: $userId")
        } catch (error: NoSuchElementException) {
            throw error
        } catch (error: Exception) {
            throw Exception("Failed to fetch user profile: ${error.message}", error)
        }
    }

    /**
     * Get the user's friends.
     *
     * RLS filters what data is returned, so only friends relevant to the user should be returned.
     *
     * @return A list of [UserFriendsDto] objects representing the user's friends.
     * @throws Exception if the friends list cannot be fetched or decoded.
     * @author frigvid
     * @created 2024-10-22
     */
    suspend fun fetchFriends(): List<UserFriendsDto> {
        return try {
            supabase.postgrest.rpc(
                function = "friend_get_all_friends"
            ).decodeList<UserFriendsDto>()
        } catch (error: Exception) {
            throw Exception("Failed to fetch user's friends: ${error.message}", error)
        }
    }

    /**
     * Get the user's pending friend requests.
     *
     * RLS filters what data is returned, so only friend requests relevant to the user should
     * be returned.
     *
     * @return A list of [UserFriendRequestsDto] objects representing the user's pending friend requests.
     * @throws Exception if the friend requests cannot be fetched or decoded.
     * @author frigvid
     * @created 2024-10-22
     */
    suspend fun fetchFriendRequests(): List<UserFriendRequestsDto> {
        return try {
            supabase.postgrest.rpc(
                function = "friend_request_get_all"
            ).decodeList<UserFriendRequestsDto>()
        } catch (error: Exception) {
            throw Exception("Failed to fetch user's friend requests: ${error.message}", error)
        }
    }

    /**
     * Check the user's admin status using a database function.
     *
     * `admin_check_if_admin` is used as opposed to the simpler `admin_is_admin`, because this
     * check won't be run that often, and the extra paranoia is nice.
     *
     * See also the [SQL reference sheet](https://raw.githubusercontent.com/frigvid/app2000-gruppe11/refs/heads/master/PREREQUISITES.sql).
     *
     * @returns The boolean of `auth.users.is_super_admin`.
     * @throws Exception if the admin status cannot be determined.
     * @author frigvid
     * @created 2024-10-22
     */
    suspend fun checkAdminStatus(userId: String): Boolean {
        return supabase.postgrest.rpc(
            function = "admin_check_if_admin",
            parameters = mapOf("user_to_check" to userId)
        ).decodeAs<Boolean>()
    }

    /**
     * Gets the user's ID.
     *
     * TODO: Consider switching out with `supabase.auth.retrieveUserForCurrentSession(updateSession = true)`
     *
     * @return The user's `auth.users.id` UUID if logged in, null otherwise.
     * @author frigvid
     * @created 2024-10-22
     */
    fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }
}
