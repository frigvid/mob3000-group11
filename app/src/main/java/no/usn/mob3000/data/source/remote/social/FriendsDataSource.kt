package no.usn.mob3000.data.source.remote.social

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.social.FriendSingleDto
import no.usn.mob3000.data.network.SupabaseClientWrapper

open class FriendsDataSource(
    private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient())
{
    suspend fun fetchAllFriends(): List<FriendSingleDto> = withContext(Dispatchers.IO) {
        supabaseClient
            .from("friends")
            .select()
            .decodeList()
    }
}
