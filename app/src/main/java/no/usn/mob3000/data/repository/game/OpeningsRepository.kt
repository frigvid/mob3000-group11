package no.usn.mob3000.data.repository.game

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import no.usn.mob3000.data.model.game.OpeningsDto
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.data.source.remote.game.OpeningsDataSource
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.helper.game.mapToDomain
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.domain.repository.game.IOpeningsRepository

/**
 * The openings repository orchestrates opening-related data operations between
 * the domain layer and the data source. It aggregates data from multiple
 * operations into cohesive results.
 *
 * ## Note
 *
 * If this class happens to say it's not in use, do note that it's being used via its
 * interface to increase testability.
 *
 * @property openingsDataSource The openings data source.
 * @property supabase The Supabase client.
 * @author frigvid
 * @created 2024-11-14
 */
class OpeningsRepository(
    private val openingsDataSource: OpeningsDataSource = OpeningsDataSource(),
    private val supabase: SupabaseClient = SupabaseClientWrapper.getClient()
) : IOpeningsRepository {
    /**
     * Function to insert an opening via an opening DTO.
     *
     * @param opening The [OpeningsDto] object.
     * @author frigvid
     * @created 2024-11-14
     */
    override suspend fun create(
        opening: OpeningsDto
    ): Result<Unit> {
        return try {
            supabase.postgrest
                .from("openings")
                .insert(opening)

            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Deletes an opening matching the [openingId] if you are its creator.
     *
     * ## Note
     *
     * If an administrator account calls this function, they're allowed to
     * delete any public opening (e.g. those marked with `null` as its creator)
     * and any others. Do note that RLS still applies, so an administrator only
     * really has access to their own openings and public ones.
     *
     * @param openingId The ID of the opening that is to be deleted.
     * @author frigvid
     * @created 2024-11-14
     */
    override suspend fun delete(
        openingId: String
    ) {
        try {
            Logger.d("Deleting opening with ID: $openingId")
            supabase.postgrest
                .from("openings")
                .delete {
                    filter {
                        eq("id", openingId)
                    }
                }

        } catch (error: NoSuchElementException) {
            throw error
        } catch (error: Exception) {
            throw Exception("Failed to delete the opening: ${error.message}", error)
        }
    }

    /**
     * Updates an existing opening by matching a full opening DTO to a row in `openings`.
     *
     * ## Note
     *
     * If you exclude parts of the DTO, it means this will delete that information from
     * those corresponding columns. Remember to insert a full object.
     *
     * @param opening The [OpeningsDto].
     * @author frigvid
     * @created 2024-11-14
     */
    override suspend fun update(
        opening: OpeningsDto
    ): Result<Unit> {
        return try {
            supabase.postgrest
                .from("openings")
                .update(opening) {
                    filter {
                        eq("id", opening.openingId)
                    }
                }

            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    /**
     * Gets the chess openings via the data source and maps it to the domain data class.
     *
     * @author frigvid
     * @created 2024-11-14
     */
    override suspend fun getOpenings(): List<Opening> {
        return openingsDataSource
            .getOpenings()
            .map {
                it.mapToDomain()
            }
    }

    /**
     * Gets a single chess opening via the data source and maps it to the domain data class.
     *
     * @author frigvid
     * @created 2024-11-14
     */
    override suspend fun getOpeningSingle(
        openingId: String
    ): Opening {
        return openingsDataSource
            .getOpeningSingle(openingId)
            .mapToDomain()
    }
}
