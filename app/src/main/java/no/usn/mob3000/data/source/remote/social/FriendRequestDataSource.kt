package no.usn.mob3000.data.source.remote.social

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.network.SupabaseClientWrapper
/**
 * A data source class responsible for handling friend request-related calls via Supabase.
 *
 * @param supabaseClient The Supabase client for interacting with the backend.
 * @constructor Creates an instance of [FriendRequestDataSource].
 * @author Husseinabdulameer11, 258030
 * @created 2024-11-13
 */
class FriendRequestDataSource (private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient()){
    /**
     * This function retrieves a list of all friend requests where the user is the "to_user".
     *
     * @param userId The ID of the user to fetch friend requests for.
     * @return A list of [FriendRequestsDto].
     * @author 258030
     * @created 2024-11-15
     */
    suspend fun fetchFriendRequests(userId: String): List<FriendRequestsDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("friend_requests")
            .select()
            { filter { eq("to_user", userId) } }
            .decodeList()
    }

    /**
     * This function retrieves a list of all friend requests where the user is the "by_user" or "to_user".
     *
     * @param userId The ID of the user to fetch friend requests for.
     * @return A list of [FriendRequestsDto].
     * @author 258030
     * @created 2024-11-17
     */
    suspend fun fetchAllFriendRequests(userId: String): List<FriendRequestsDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("friend_requests")
            .select {
                filter {
                    or {
                        eq("by_user", userId)
                        eq("to_user", userId)
                    }
                }
            }
            .decodeList()
    }
    /**
     * This function retrieves a single friend request based on the given ID, used for getting a specific request
     * for further actions.
     *
     * @param friendRequestId The ID of the friend request to fetch.
     * @return An instance of [FriendRequestsDto] if found, otherwise null.
     * @author 258030
     * @created 2024-11-15
     */
    suspend fun fetchFriendRequestById(friendRequestId: String): FriendRequestsDto? = withContext(Dispatchers.IO) {
        supabaseClient
            .from("friend_requests")
            .select()
            {
                filter { eq("id", friendRequestId) }
            }
            .decodeSingleOrNull()
    }
    /**
     * Removes a a friend request based on the requestId.  Used for removing a friend request after it has been either accepted or cancelled.
     *
     * @param friendRequestId The ID of the friend request to delete.
     * @author Husseinabdulameer11
     * @created 2024-11-13
     */
    suspend fun deleteFriendRequestById(friendRequestId: String): PostgrestResult = withContext(Dispatchers.IO) {
        supabaseClient
            .from("friend_requests")
            .delete { filter { eq("id", friendRequestId) } }
    }
    /**
     * Used when the user sends a friend request. Creates a new row in the friend_requests table, where the "requested" friend have the option of accepting or
     * declining the invitation.
     *
     * @param friendRequestItem An instance of [FriendRequestsDto] containing the data for the new friend request.
     * @author Husseinabdulameer11
     * @created 2024-11-13
     */
    suspend fun insertFriendRequest(friendRequestItem: FriendRequestsDto): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient.from("friend_requests").insert(friendRequestItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    /**
     * This function is used after a friend request is accepted, to join the two users in a row.
     *
     * @param friend The [FriendsDto] object containing the data for the new friend relationship.
     * @author 258030
     * @created 2024-11-14
     */
    suspend fun insertFriend(friend: FriendsDto): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient
                .from("friends")
                .insert(friend)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
