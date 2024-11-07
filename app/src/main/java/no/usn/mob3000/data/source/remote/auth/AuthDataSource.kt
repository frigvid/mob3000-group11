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
     * Check the user's admin status using a database function.
     *
     * `admin_check_if_admin` is used as opposed to the simpler `admin_is_admin`, because this
     * check won't be run that often, and the extra paranoia is nice.
     *
     * See also the [SQL reference sheet](https://raw.githubusercontent.com/frigvid/app2000-gruppe11/refs/heads/master/PREREQUISITES.sql).
     *
     * @returns The boolean of `auth.users.is_super_admin`.
     * @throws Exception if the admin status cannot be determined.
     * @author frigvid
     * @created 2024-10-22
     */
    suspend fun checkAdminStatus(userId: String): Boolean {
        return supabase.postgrest.rpc(
            function = "admin_check_if_admin",
            parameters = mapOf("user_to_check" to userId)
        ).decodeAs<Boolean>()
    }

    /**
     * Gets the user's ID.
     *
     * @return The user's `auth.users.id` UUID if logged in, null otherwise.
     * @author frigvid
     * @created 2024-10-22
     */
    fun getCurrentUserId(): String = supabase.auth.currentUserOrNull()?.id.toString()
}
