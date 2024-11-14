package no.usn.mob3000.domain.model.game

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID

/**
 * The chess opening struct.
 *
 * @property id The opening's ID.
 * @property createdBy The user's UUID. Null for "public."
 * @property title The title of the opening.
 * @property description The description of the opening.
 * @property moves The PGN notation for moves.
 * @property createdAt A timestamp representing when the opening was created.
 * @author frigvid
 * @created 2024-11-14
 */
data class Opening(
    val id: String = UUID.randomUUID().toString(),
    val createdBy: String? = null,
    val title: String? = "\uD83D\uDC4B\uD83D\uDE00",
    val description: String? = "",
    val moves: String? = null,
    val createdAt: Instant = Clock.System.now()
)
