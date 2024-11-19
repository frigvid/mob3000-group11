package no.usn.mob3000.data.source.remote.social

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.network.SupabaseClientWrapper

/**
 * Data source responsible for handling update data handling for the user profile. Other data related
 * to the user are handled in [UserDataSource]
 *
 * @param supabaseClient The Supabase client for making API requests.
 *
 * @author 258030
 * @created 2024-11-09
 */
class ProfileUserDataSource(
    private val supabaseClient: SupabaseClient = SupabaseClientWrapper.getClient()
) {
    /**
     * Updates the user profile.
     *
     * @param userId The user's ID.
     */
    suspend fun updateProfile(userId: String, profileDto: ProfileDto): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            supabaseClient
                .from("profiles")
                .update(profileDto) {
                    filter {
                        eq("id", userId)
                    }
                }
            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
