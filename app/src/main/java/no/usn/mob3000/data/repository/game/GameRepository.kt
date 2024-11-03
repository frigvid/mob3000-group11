package no.usn.mob3000.data.repository.game

import io.github.jan.supabase.SupabaseClient
import no.usn.mob3000.data.network.SupabaseClientWrapper

/**
 * @author frigvid
 * @created 2024-11-01
 */
class AuthRepository(
    private val supabase: SupabaseClient = SupabaseClientWrapper.getClient()
) {
    // NOTE: array PGNs are stored in openings.
    fun arrayPgnToPgn() {
        TODO("Not yet implemented.")
    }

    fun pgnToFen() {
        TODO("Not yet implemented.")
    }

    fun fenToPgn() {
        TODO("Not yet implemented.")
    }
}
