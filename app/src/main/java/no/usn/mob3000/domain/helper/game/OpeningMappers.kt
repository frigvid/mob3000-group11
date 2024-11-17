package no.usn.mob3000.domain.helper.game

import no.usn.mob3000.data.model.game.OpeningsDto
import no.usn.mob3000.domain.model.game.opening.Opening

/**
 * Extension function to map a DTO opening to a pure kotlin data class opening.
 *
 * @author frigvid
 * @created 2024-11-14
 */
fun OpeningsDto.mapToDomain(): Opening = Opening(
    id = this.openingId,
    createdBy = this.createdByUser,
    title = this.title,
    description = this.content,
    moves = convertJsonPgnArrayToPgn(this.pgn),
    createdAt = this.createdAt
)

/**
 * Extension function to map an pure kotlin data class opening to a DTO opening.
 *
 * @author frigvid
 * @created 2024-11-14
 */
fun Opening.mapToData(): OpeningsDto = OpeningsDto(
    openingId = this.id,
    createdByUser = this.createdBy,
    title = this.title,
    content = this.description,
    pgn = convertPgnToJsonPgnArray(this.moves),
    createdAt = this.createdAt
)
