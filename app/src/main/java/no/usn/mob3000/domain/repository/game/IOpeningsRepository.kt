package no.usn.mob3000.domain.repository.game

import no.usn.mob3000.data.model.game.OpeningsDto
import no.usn.mob3000.domain.model.game.opening.Opening

/**
 * Interface for the openings repository.
 *
 * @author frigvid
 * @created 2024-11-14
 */
interface IOpeningsRepository {
    suspend fun create(opening: OpeningsDto): Result<Unit>
    suspend fun delete(openingId: String)
    suspend fun update(opening: OpeningsDto): Result<Unit>
    suspend fun getOpenings(): List<Opening>
    suspend fun getOpeningSingle(openingId: String): Opening
}
