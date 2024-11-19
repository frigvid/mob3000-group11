package no.usn.mob3000.data.source.remote.social

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.network.SupabaseClientWrapper

/**
 * Data source responsible for handling friend fetch calls via Supabase.
 *
 * @param supabaseClient The Supabase client instance.
 * @author 258030
 * @contributor Husseinabdulameer11
 * @created: 2024-11-04
 */
class FriendsDataSource(
    private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient())
{
    /**
     * Fetches all rows from the friends table.
     *
     * @return A list of [FriendsDto]
     * @author Husseinabdulameer11
     * @created 2024-11-04
     */
    suspend fun fetchFriends(): List<FriendsDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("friends")
            .select()
            .decodeList()
    }

    /**
     * Fetches all rows that don't corresponds to the current user.
     *
     * @param userId The user id of the current user. Used to filter out friend connections that does not exist
     * @return A list of [ProfileDto]
     * @author 258030
     * @created 2024-11-15
     */
    suspend fun fetchNonFriends(userId: String): List<ProfileDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("profiles")
            .select {
                filter {
                    neq("id", userId)
                }
            }
            .decodeList()
    }
}
