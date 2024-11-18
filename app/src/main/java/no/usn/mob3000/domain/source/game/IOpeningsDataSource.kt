package no.usn.mob3000.domain.source.game

import no.usn.mob3000.data.model.game.OpeningsDto

/**
 * Interface for the openings data source.
 *
 * @author frigvid
 * @created 2024-11-14
 */
interface IOpeningsDataSource {
    suspend fun getOpenings(): List<OpeningsDto>
    suspend fun getOpeningSingle(openingId: String): OpeningsDto
}
