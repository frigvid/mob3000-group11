package no.usn.mob3000.data.source.remote.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.gotrue.user.UserSession
import io.github.jan.supabase.postgrest.from
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.data.model.game.GameDataDto
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.data.model.social.FriendSingleDto
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.domain.source.IAuthDataSource

/**
 * The data source for authentication-related data-fetching operations.
 *
 * @property supabase The Supabase client.
 * @author frigvid
 * @created 2024-10-22
 */
class AuthDataSource(
    private val supabase: SupabaseClient = SupabaseClientWrapper.getClient()
) : IAuthDataSource {
    /**
     * Gets the user's current session, if any.
     *
     * @return The user's [UserSession] object or null.
     * @author frigvid
     * @created 2024-10-28
     */
    override suspend fun getCurrentSession(): UserSession? = supabase.auth.currentSessionOrNull()

    /**
     * Gets the full user object for the current user.
     *
     * TODO: Consider replacing [[getCurrentUserId]] with this in the `AuthRepository`.
     *
     * @return The user's [UserInfo] object.
     * @throws Exception if the user cannot be fetched or decoded.
     * @author frigvid
     * @created 2024-10-22
     */
    override suspend fun getCurrentUser(): UserInfo {
        return supabase.auth.retrieveUserForCurrentSession(updateSession = true)
    }

    /**
     * Gets the user's game stats.
     *
     *
     */
    override suspend fun getUserGameStats(): GameDataDto {
        return try {
            supabase.from("gamedata").select() {
                filter {
                    eq("id", getCurrentUserId())
                }
            }.decodeSingleOrNull<GameDataDto>()
                // TODO: Extract string resource.
                ?: throw NoSuchElementException("No game data found for the user.")
        } catch (error: NoSuchElementException) {
            throw error
        } catch (error: Exception) {
            // TODO: Extract string resource.
            throw Exception("Failed to fetch user's game data: ${error.message}", error)
        }
    }

    /**
     * Get the user's profile.
     *
     * @param userId The user's `auth.users.id` UUID.
     * @return The user's [ProfileDto] data.
     * @throws NoSuchElementException if no such profile exists for the inputted UUID.
     * @throws Exception if the profile cannot be fetched or decoded.
     * @author frigvid
     * @created 2024-10-22
     */
    override suspend fun getUserProfile(userId: String): ProfileDto? {
        return try {
            supabase.from("profiles").select() {
                filter {
                    eq("id", userId)
                }
            }.decodeSingleOrNull<ProfileDto>()
                // TODO: Extract string resource.
                ?: throw NoSuchElementException("No profile found for user: $userId")
        } catch (error: NoSuchElementException) {
            throw error
        } catch (error: Exception) {
            // TODO: Extract string resource.
            throw Exception("Failed to fetch user profile: ${error.message}", error)
        }
    }

    /**
     * Get the user's friends.
     *
     * RLS filters what data is returned, so only friends relevant to the user should be returned.
     *
     * @param userId The user's `auth.users.id` UUID.
     * @return A list of [FriendsDto] objects representing the user's friends.
     * @throws Exception if the friends list cannot be fetched or decoded.
     * @author frigvid
     * @created 2024-10-22
     */
    override suspend fun getUserFriends(userId: String): List<FriendsDto> {
        return try {
            //supabase.postgrest.rpc(
            //    function = "friend_get_all_friends"
            //).decodeList<FriendsDto>()

            supabase.from("friends").select() {
                filter {
                    or {
                        eq("user1", userId)
                        eq("user2", userId)
                    }
                }
            }.decodeList<FriendsDto>()
        } catch (error: Exception) {
            // TODO: Extract string resource.
            throw Exception("Failed to fetch user's friends: ${error.message}", error)
        }
    }

    /**
     * Get the user's friend data. Single.
     *
     * @param friendUserId The friend's UUID.
     * @author frigvid
     * @created 2024-10-28
     */
    suspend fun getUserFriendSingle(friendUserId: String): FriendSingleDto {
        return try {
            supabase.postgrest.rpc(
                function = "friend_get_one",
                parameters = mapOf("friend" to friendUserId)
            ).decodeSingleOrNull<FriendSingleDto>()
                // TODO: Extract string resource.
                ?: throw NoSuchElementException("No friendship found for user: $friendUserId")
        } catch (error: NoSuchElementException) {
            throw error
        } catch (error: Exception) {
            // TODO: Extract string resource.
            throw Exception("Failed to fetch user's friends: ${error.message}", error)
        }
    }

    /**
     * Get the user's pending friend requests.
     *
     * RLS filters what data is returned, so only friend requests relevant to the user should
     * be returned.
     *
     * @return A list of [FriendRequestsDto] objects representing the user's pending friend requests.
     * @throws Exception if the friend requests cannot be fetched or decoded.
     * @author frigvid
     * @created 2024-10-22
     */
    override suspend fun getUserFriendRequests(): List<FriendRequestsDto> {
        return try {
            supabase.postgrest.rpc(
                function = "friend_request_get_all"
            ).decodeList<FriendRequestsDto>()
        } catch (error: Exception) {
            // TODO: Extract string resource.
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
    fun getCurrentUserId(): String = supabase.auth.currentUserOrNull()?.id.toString()
}
