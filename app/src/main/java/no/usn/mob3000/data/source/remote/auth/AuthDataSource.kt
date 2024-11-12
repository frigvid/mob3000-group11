package no.usn.mob3000.data.source.remote.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.gotrue.user.UserSession
import no.usn.mob3000.data.network.SupabaseClientWrapper
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
     * Gets the user's ID.
     *
     * @return The user's `auth.users.id` UUID if logged in, null otherwise.
     * @author frigvid
     * @created 2024-10-22
     */
    fun getCurrentUserId(): String = supabase.auth.currentUserOrNull()?.id.toString()

    /**
     * Check the user's admin status using a database function.
     *
     * `admin_check_if_admin` is used as opposed to the simpler `admin_is_admin`, because this
     * check won't be run that often, and the extra paranoia is nice.
     *
     * See also the [SQL reference sheet](https://raw.githubusercontent.com/frigvid/app2000-gruppe11/refs/heads/master/PREREQUISITES.sql).
     *
     * @param userId The UUID of the user to check.
     * @returns The boolean of `auth.users.is_super_admin`.
     * @throws Exception if the admin status cannot be determined.
     * @author frigvid
     * @created 2024-10-22
     */
    suspend fun checkAdminStatus(
        userId: String
    ): Boolean {
        return try {
            supabase.postgrest.rpc(
                function = "admin_check_if_admin",
                parameters = mapOf("user_to_check" to userId)
            ).decodeAs<Boolean>()
        } catch (error: Exception) {
            false
        }
    }

    /**
     * Overloaded function to check the authenticated user's admin status only.
     *
     * See [checkAdminStatus] for more details.
     *
     * @author frigvid
     * @created 2024-11-07
     */
    suspend fun checkAdminStatus(): Boolean {
        return try {
            supabase.postgrest.rpc(
                function = "admin_check_if_admin",
                parameters = mapOf("user_to_check" to getCurrentUserId())
            ).decodeAs<Boolean>()
        } catch (error: Exception) {
            false
        }
    }

    /**
     * Check if there is an authenticated user or not.
     *
     * NOTE: Could probably do with being a tad less simple, but I think this will be functional
     *       enough for now.
     *
     * @author frigvid
     * @created 2024-11-07
     */
    fun checkAuthStatus(): Boolean = supabase.auth.currentUserOrNull() != null


}
