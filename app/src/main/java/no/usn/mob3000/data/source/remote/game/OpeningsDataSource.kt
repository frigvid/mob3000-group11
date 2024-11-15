package no.usn.mob3000.data.source.remote.game

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import no.usn.mob3000.data.model.game.OpeningsDto
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.domain.source.game.IOpeningsDataSource

/**
 * The data source for fetching openings related data.
 *
 * @property supabase The Supabase client.
 * @author frigvid
 * @created 2024-11-14
 */
class OpeningsDataSource(
    private val supabase: SupabaseClient = SupabaseClientWrapper.getClient()
) : IOpeningsDataSource {
    /**
     * Get all openings.
     *
     * @author frigvid
     * @created 2024-11-14
     */
    override suspend fun getOpenings(): List<OpeningsDto> {
        return try {
            supabase
                .from("openings")
                .select()
                .decodeList<OpeningsDto>()
        } catch (error: NoSuchElementException) {
            throw error
        } catch (error: Exception) {
            throw Exception("Failed to fetch the openings: ${error.message}", error)
        }
    }

    /**
     * Gets a single opening from the openings table.
     *
     * @param openingId The opening's ID.
     * @author frigvid
     * @created 2024-11-14
     */
    override suspend fun getOpeningSingle(openingId: String): OpeningsDto {
        return try {
            supabase.postgrest.rpc(
                function = "opening_get",
                parameters = mapOf("openingId" to "opn_id")
            ).decodeSingleOrNull<OpeningsDto>()
                ?: throw NoSuchElementException("No opening found for opening ID: $openingId")
        } catch (error: NoSuchElementException) {
            throw error
        } catch (error: Exception) {
            throw Exception("Failed to fetch the opening: ${error.message}", error)
        }
    }
}
