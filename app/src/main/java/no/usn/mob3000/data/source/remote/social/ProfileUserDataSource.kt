package no.usn.mob3000.data.source.remote.social

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.network.SupabaseClientWrapper

/**
 * TODO: KDoc
 *
 * @author 258030
 * @created 2024-11-09
 */
class ProfileUserDataSource(
    private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient()
) {
    suspend fun fetchUserById(userId: String): ProfileDto? = withContext(Dispatchers.IO) {
        supabaseClient
            .from("profiles")
            .select()
            { filter { eq("id", userId) } }
            .decodeSingleOrNull()
    }
    suspend fun updateProfile(userId: String, profileDto: ProfileDto): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient
                .from("profiles")
                .update(profileDto) {
                    filter { eq("id", userId) }
                }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * TODO: SAME SOLUTION FOR FETCHING FRIENDS AND NON-FRIENDS
     */
    suspend fun fetchNonFriends(userId: String): List<ProfileDto> = withContext(Dispatchers.IO) {
        val friendsList = supabaseClient
            .from("friends")
            .select()
            {
                filter {
                    or {
                        eq("user1", userId)
                        eq("user2", userId)
                    }
                }
            }
            .decodeList<FriendsDto>()
        val friendIds = friendsList.flatMap { listOf(it.user1, it.user2) }.filter { it != userId }
        Log.d("fetchNonFriends", "Friend IDs: $friendIds")
        val allProfiles = supabaseClient
            .from("profiles")
            .select()
            {
                filter {
                    neq("id", userId)
                }
            }
            .decodeList<ProfileDto>()
        val nonFriendProfiles = allProfiles.filter { it.userId !in friendIds }
        nonFriendProfiles.forEach { profile ->
            Log.d("fetchNonFriends", "Non-Friend Profile: ${profile.userId}, Display Name: ${profile.displayName}")
        }
        nonFriendProfiles
    }





}
