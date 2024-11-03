package no.usn.mob3000.data.repository.content

import io.github.jan.supabase.gotrue.auth
import no.usn.mob3000.data.network.SupabaseClientWrapper.getClient

/**
 * Retrieves the current user's ID. I guess this exist somewhere else in the code, so pinpoint me to where later.
 * @return The current user's ID.
 * @author 258030
 * @created 2024-10-28
 */
fun getUserId(): String? {

    val client = getClient()
    val userId = client.auth.currentSessionOrNull()?.user?.id ?: "user-id"
    return userId
}
