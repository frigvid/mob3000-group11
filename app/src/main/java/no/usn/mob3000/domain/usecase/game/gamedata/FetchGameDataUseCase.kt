package no.usn.mob3000.domain.usecase.game.gamedata

import no.usn.mob3000.data.model.game.GameDataDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.domain.source.game.IGameDataSource

/**
 * Android use case for fetching game data.
 *
 * @author frigvid
 * @created 2024-11-17
 */
class FetchGameDataUseCase(
    private val gameDataSource: IGameDataSource,
    private val authDataSource: AuthDataSource = AuthDataSource()
) {
    /**
     * Fetches game data for a specific user.
     *
     * @param userId Optional UUID. If null, fetches authenticated user's data.
     * @return The user's game data on success.
     * @author frigvid
     * @created 2024-11-17
     */
    suspend operator fun invoke(userId: String? = null): Result<GameDataDto> = try {
        val targetUserId = userId ?: authDataSource.getCurrentUserId()
        Result.success(gameDataSource.getUserGameData(targetUserId))
    } catch (error: Exception) {
        Result.failure(error)
    }

}
