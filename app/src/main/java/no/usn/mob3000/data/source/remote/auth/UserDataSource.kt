package no.usn.mob3000.data.source.remote.auth;

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.game.GameDataDto
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.data.model.social.FriendSingleDto
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.domain.source.IUserDataSource

/**
 * The data source for user-related data-fetching operations.
 *
 * @property supabase The Supabase client.
 * @property authDataSource The authentication data source.
 * @author frigvid
 * @created 2024-11-07
 */
class UserDataSource(
    private val supabase: SupabaseClient = SupabaseClientWrapper.getClient(),
    private val authDataSource: AuthDataSource = AuthDataSource()
) : IUserDataSource {
    /**
     * Gets the full user object for the current user.
     *
     * @return The user's [UserInfo] object.
     * @throws Exception if the user cannot be fetched or decoded.
     * @author frigvid
     * @created 2024-10-22
     */
    override suspend fun getCurrentUser(): UserInfo = supabase.auth.retrieveUserForCurrentSession(updateSession = true)

    /**
     * Gets the user's game stats.
     *
     * @author frigvid
     * @created 2024-10-22
     */
    override suspend fun getUserGameStats(): GameDataDto {
        return try {
            supabase
                .from("gamedata")
                .select {
                    filter {
                        eq("id", authDataSource.getCurrentUserId())
                    }
                }.decodeSingleOrNull<GameDataDto>()
                    ?: throw NoSuchElementException("No game data found for the user.")
        } catch (error: NoSuchElementException) {
            throw error
        } catch (error: Exception) {
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
            supabase
                .from("profiles")
                .select {
                filter {
                    eq("id", userId)
                }
            }.decodeSingleOrNull<ProfileDto>()
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
     * @param userId The user's `auth.users.id` UUID.
     * @return A list of [FriendsDto] objects representing the user's friends.
     * @throws Exception if the friends list cannot be fetched or decoded.
     * @author frigvid
     * @created 2024-10-22
     */
    override suspend fun getUserFriends(userId: String): List<FriendsDto> {
        return try {
            supabase
                .from("friends")
                .select {
                filter {
                    or {
                        eq("user1", userId)
                        eq("user2", userId)
                    }
                }
            }.decodeList<FriendsDto>()
        } catch (error: Exception) {
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
                ?: throw NoSuchElementException("No friendship found for user: $friendUserId")
        } catch (error: NoSuchElementException) {
            throw error
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
            throw Exception("Failed to fetch user's friend requests: ${error.message}", error)
        }
    }
    /**
     * Gets the full user object for the current user.
     *
     * @param userId The user's UUID.
     * @return The user's [UserInfo] object.
     * @throws Exception if the user cannot be fetched or decoded.
     * @author Husseinabdulameer11
     * @created 2024-11-03
     */
    suspend fun fetchUserById(userId: String): ProfileDto? = withContext(Dispatchers.IO) {
        supabase
            .from("profiles")
            .select {
                filter {
                    eq("id", userId)
                }
            }.decodeSingleOrNull()
    }
}
