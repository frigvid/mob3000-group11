package no.usn.mob3000.data.source.remote.game

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import no.usn.mob3000.data.model.game.GameDataDto
import no.usn.mob3000.data.network.SupabaseClientWrapper
import no.usn.mob3000.domain.source.game.IGameDataSource
import java.util.Locale.filter

/**
 * The data source for getting the user's game data.
 *
 * Technically this should be named `GameDataDataSource` if we were aiming for accuracy,
 * but that sounds kind of stupid, so we're keeping it as is.
 *
 * @property supabase The Supabase client.
 * @author frigvid
 * @created 2024-11-17
 */
class GameDataSource(
    private val supabase: SupabaseClient = SupabaseClientWrapper.getClient()
) : IGameDataSource {
    /**
     * Gets a user's game statistics from gamedata.
     *
     * @param userId The UUID of the user whose stats to fetch.
     * @return The user's [GameDataDto] data.
     * @throws NoSuchElementException if no stats exist for the user.
     * @throws Exception if fetching fails for other reasons.
     * @author frigvid
     * @created 2024-11-17
     */
    override suspend fun getUserGameData(
        userId: String
    ): GameDataDto {
        return try {
            supabase
                .from("gamedata")
                .select() {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingleOrNull<GameDataDto>()
                    ?: throw NoSuchElementException("No game data found for user: $userId")
        } catch (error: NoSuchElementException) {
            throw error
        } catch (error: Exception) {
            throw Exception("Failed to fetch game data: ${error.message}", error)
        }
    }

    /**
     * Updates a user's game statistics in the `gamedata` table.
     *
     * Since users have full read to the `gamedata` table, that necessitates
     * filtering by user ID.
     *
     * @param userId The UUID of the user whose stats to update.
     * @param gameData The updated [GameDataDto].
     * @return Unit on success.
     * @throws Exception if the update fails.
     * @author frigvid
     * @created 2024-11-17
     */
    override suspend fun updateUserGameData(
        userId: String,
        gameData: GameDataDto
    ): Result<Unit> {
        return try {
            supabase
                .from("gamedata")
                .update(gameData) {
                    filter { eq("id", userId) }
                }
            Result.success(Unit)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
