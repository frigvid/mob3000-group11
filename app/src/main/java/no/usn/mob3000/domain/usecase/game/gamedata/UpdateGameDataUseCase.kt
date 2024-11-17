package no.usn.mob3000.domain.usecase.game.gamedata

import no.usn.mob3000.data.model.game.GameDataDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.domain.source.game.IGameDataSource

/**
 * Android use case for updating game data.
 *
 * @author frigvid
 * @created 2024-11-17
 */
class UpdateGameDataUseCase(
    private val gameDataSource: IGameDataSource,
    private val authDataSource: AuthDataSource = AuthDataSource()
) {
    /**
     * Updates game data for the authenticated user.
     *
     * @param gameData The updated game data.
     * @return Unit on success.
     * @author frigvid
     * @created 2024-11-17
     */
    suspend operator fun invoke(
        gameData: GameDataDto
    ): Result<Unit> = try {
        gameDataSource.updateUserGameData(authDataSource.getCurrentUserId(), gameData)
    } catch (error: Exception) {
        Result.failure(error)
    }
}
