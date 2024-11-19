package no.usn.mob3000.data.source.remote.game

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import no.usn.mob3000.data.model.game.RepertoireDto
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.domain.source.game.IGroupsDataSource

/**
 * The data source for fetching openings related data.
 *
 * @property supabase The Supabase client.
 * @author frigvid
 * @created 2024-11-15
 */
class GroupsDataSource(
    private val supabase: SupabaseClient = SupabaseClientWrapper.getClient()
) : IGroupsDataSource {
    /**
     * Get all repertoires/groups.
     *
     * @author frigvid
     * @created 2024-11-15
     */
    override suspend fun getGroups(): List<RepertoireDto> {
        return try {
            supabase
                .from("repertoire")
                .select()
                .decodeList<RepertoireDto>()
        } catch (error: NoSuchElementException) {
            throw error
        } catch (error: Exception) {
            throw Exception("Failed to fetch the groups: ${error.message}", error)
        }
    }

    /**
     * Gets a single repertoire/group from the repertoire table.
     *
     * @param groupId The repertoire's/group's ID.
     * @author frigvid
     * @created 2024-11-15
     */
    override suspend fun getGroupSingle(
        groupId: String
    ): RepertoireDto {
        return try {
            supabase
                .from("repertoire")
                .select{
                    filter {
                        eq("id", groupId)
                    }
                }
                .decodeSingleOrNull<RepertoireDto>()
                    ?: throw NoSuchElementException("No repertoire/group found for ID: $groupId")
        } catch (error: NoSuchElementException) {
            throw error
        } catch (error: Exception) {
            throw Exception("Failed to fetch the opening: ${error.message}", error)
        }
    }
}
