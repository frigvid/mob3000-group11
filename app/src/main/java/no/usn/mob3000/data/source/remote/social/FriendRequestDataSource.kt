package no.usn.mob3000.data.source.remote.social

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.content.DocsDto
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.network.SupabaseClientWrapper

/**
 * @author Husseinabdulameer11
 *
 */

class FriendRequestDataSource (private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient()){

    suspend fun fetchAllFriendRequests(userId: String): List<FriendRequestsDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("friend_requests")
            .select()
            {
                filter {
                    eq("to_user", userId)
                }
            }
            .decodeList()
    }

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
     * @author Husseinabdulameer11
     * deletes a friendrequest based on id (used when friend request gets declined or accepted since either way it needs to be removed from the screen to either the friends list or completly deleted
     */
    suspend fun deleteFriendRequestById(friendRequestId: String): PostgrestResult = withContext(Dispatchers.IO) {
        supabaseClient
            .from("friend_requests")
            .delete { filter { eq("id", friendRequestId) } }
    }


    /**
     * @author Husseinabdulameer11
     * used when the user are gonna try to add a friend
     */
    suspend fun insertFriendRequest(friendrequestItem: FriendRequestsDto): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient.from("friend_requests").insert(friendrequestItem)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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
