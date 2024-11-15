package no.usn.mob3000.data.repository.game

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import no.usn.mob3000.data.model.game.OpeningsDto
import no.usn.mob3000.data.model.game.RepertoireDto
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.data.source.remote.game.GroupsDataSource
import no.usn.mob3000.domain.helper.game.mapToDomain
import no.usn.mob3000.domain.model.game.Group
import no.usn.mob3000.domain.repository.game.IGroupsRepository

/**
 * The repertoire/group repository orchestrates repertoire/group-related data operations
 * between the domain layer and the data source.
 *
 * It aggregates data from multiple operations into cohesive results.
 *
 * @property openingsDataSource The openings data source.
 * @property supabase The Supabase client.
 * @author frigvid
 * @created 2024-11-14
 */
class GroupsRepository(
    private val groupsDataSource: GroupsDataSource = GroupsDataSource(),
    private val supabase: SupabaseClient = SupabaseClientWrapper.getClient()
) : IGroupsRepository {
    /**
     * Function to insert a repertoire/group via a repertoire DTO.
     *
     * @param group The [RepertoireDto] object.
     * @author frigvid
     * @created 2024-11-15
     */
    override suspend fun create(
        group: RepertoireDto
    ): Result<Unit> {
        return try {
            supabase.postgrest
                .from("repertoire")
                .insert(group)

            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Deletes a repertoire/group matching the [groupId], if you are its creator.
     *
     * @param groupId The ID of the repertoire/group that is to be deleted.
     * @author frigvid
     * @created 2024-11-15
     */
    override suspend fun delete(
        groupId: String
    ) {
        try {
            Log.d("GroupsRepository", "Deleting repertoire/group with ID: $groupId")
            supabase.postgrest
                .from("repertoire")
                .delete {
                    filter {
                        eq("id", groupId)
                    }
                }

        } catch (error: NoSuchElementException) {
            throw error
        } catch (error: Exception) {
            throw Exception("Failed to delete the repertoire/group: ${error.message}", error)
        }
    }

    /**
     * Updates an existing repertoire/group by matching a full repertoire DTO to a row in `repertoire`.
     *
     * ## Note
     *
     * If you exclude parts of the DTO, it means this will delete that information from
     * those corresponding columns. Remember to insert a full object.
     *
     * @param group The [RepertoireDto].
     * @author frigvid
     * @created 2024-11-15
     */
    override suspend fun update(
        group: RepertoireDto
    ): Result<Unit> {
        return try {
            supabase.postgrest
                .from("repertoire")
                .update(group) {
                    filter {
                        eq("id", group.repertoireId)
                    }
                }

            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Get all repertoires/groups available to the user via the data source, and map them to their
     * corresponding pure kotlin data class group object.
     *
     * @author frigvid
     * @created 2024-11-15
     */
    override suspend fun getGroups(): List<Group> {
        return groupsDataSource
            .getGroups()
            .map {
                it.mapToDomain()
            }
    }

    /**
     * Get a single repertoire/group via the data source, and map it to its corresponding pure
     * kotlin data class group object.
     *
     * @author frigvid
     * @created 2024-11-15
     */
    override suspend fun getGroupSingle(
        groupId: String
    ): Group {
        return groupsDataSource
            .getGroupSingle(groupId)
            .mapToDomain()
    }

}
