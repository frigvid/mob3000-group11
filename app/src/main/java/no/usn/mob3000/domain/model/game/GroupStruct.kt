package no.usn.mob3000.domain.model.game

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID

/**
 * The chess repertoire/group struct.
 *
 * @property id The repertoire/group's ID.
 * @property createdBy The user's UUID.
 * @property title The title of the opening.
 * @property description The description of the opening.
 * @property openings A list of all openings that are part of a group.
 * @property createdAt A timestamp representing when the opening was created.
 * @author frigvid
 * @created 2024-11-14
 */
data class Group(
    val id: String = UUID.randomUUID().toString(),
    val createdBy: String,
    val title: String? = "\uD83D\uDC4B\uD83D\uDE00",
    val description: String? = "",
    val openings: List<Opening>,
    val createdAt: Instant = Clock.System.now()
)
