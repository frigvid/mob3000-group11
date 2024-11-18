package no.usn.mob3000.domain.source.game

import no.usn.mob3000.data.model.game.GameDataDto

/**
 * The interface for the game data source.
 *
 * @author frigvid
 * @created 2024-11-17
 */
interface IGameDataSource {
    suspend fun getUserGameData(userId: String): GameDataDto
    suspend fun updateUserGameData(userId: String, gameData: GameDataDto): Result<Unit>
}
