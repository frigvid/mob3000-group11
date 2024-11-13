package no.usn.mob3000.data.source.remote.social

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.network.SupabaseClientWrapper

/**
 * TODO: KDoc
 *
 * @author 258030
 * @created 2024-11-09
 */
class UserDataSource(
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
}
