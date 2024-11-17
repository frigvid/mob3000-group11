package no.usn.mob3000.data.source.remote.admin

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import no.usn.mob3000.data.model.auth.UserDto
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.domain.source.admin.IAdminDataSource

/**
 * Administrator data source.
 *
 * @author frigvid
 * @created 2024-11-17
 */
class AdminDataSource(
    private val supabase: SupabaseClient = SupabaseClientWrapper.getClient()
) : IAdminDataSource {
    override suspend fun promoteToAdmin(userId: String) {
        supabase.postgrest.rpc(
            function = "admin_promote_to_admin",
            parameters = mapOf("user_to_promote" to userId)
        )
    }

    override suspend fun demoteToRegularUser(userId: String) {
        supabase.postgrest.rpc(
            function = "admin_demote_to_user",
            parameters = mapOf("admin_to_demote" to userId)
        )
    }

    override suspend fun deleteUser(userId: String) {
        supabase.postgrest.rpc(
            function = "admin_delete_user",
            parameters = mapOf("user_to_delete" to userId)
        )
    }

    override suspend fun fetchAllUsers(): List<UserDto> =
        supabase.postgrest.rpc(
            function = "admin_get_all_users"
        ).decodeList<UserDto>()
}
