package no.usn.mob3000.data.source.remote.social

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.content.DocsDto
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.data.network.SupabaseClientWrapper

/**
 * @author Husseinabdulameer11
 *
 */

class FriendRequestDataSource (private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient()){

    suspend fun fetchAllFriendRequests(): List<FriendRequestsDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("friend_requests")
            .select()
            .decodeList()
    }


    /**
     * @author Husseinabdulameer11
     * deletes a friendrequest based on id (used when friend request gets declined or accepted since either way it needs to be removed from the screen to either the friends list or completly deleted
     */
    suspend fun deleteFriendRequestById(friend_requestId: String): PostgrestResult = withContext(Dispatchers.IO) {
        supabaseClient
            .from("friend_requests")
            .delete { filter { eq("id", friend_requestId) } }
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


}
