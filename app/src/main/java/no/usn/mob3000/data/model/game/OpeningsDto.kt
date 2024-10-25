package no.usn.mob3000.data.model.game

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

/**
 * Serializable data transferable object representing the `public.openings` table.
 *
 * @param openingId The opening item's UUID.
 * @param createdByUser A UUID reference to a matching user in `auth.users`.
 *                      NOTE: If `null`, it's available for all users.
 * @param title The opening item's title.
 * @param content The opening item's content description.
 * @param pgn A PGN string representing the opening.
 *            WARNING: The PGN string is split into component parts and stored in a Json array.
 * @param createdAt A timestamp representing when the opening item was created.
 * @author frigvid
 * @created 2024-10-25
 */
@Serializable
data class OpeningsDto(
    @SerialName("id")
    val openingId: String,
    @SerialName("created_by")
    val createdByUser: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("description")
    val content: String? = null,
    @SerialName("pgn")
    val pgn: JsonArray? = null,
    @SerialName("timestamp")
    val createdAt: Instant
)