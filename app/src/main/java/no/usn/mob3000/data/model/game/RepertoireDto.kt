package no.usn.mob3000.data.model.game

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

/**
 * Serializable data transferable object representing the `public.repertoire` table.
 *
 * @param repertoireId The repertoire item's UUID.
 * @param createdAt A timestamp representing when the repertoire item was created.
 * @param createdBy A UUID reference to a matching user in `auth.users`.
 * @param openings A Json array containing UUID references matching a [OpeningsDto]'s object's UUID.
 * @param title The repertoire item's title.
 * @param description The repertoire item's description.
 * @author frigvid
 * @created 2024-10-25
 */
@Serializable
data class RepertoireDto(
    @SerialName("id")
    val repertoireId: String,
    @SerialName("timestamp")
    val createdAt: Instant,
    @SerialName("usr")
    val createdBy: String,
    @SerialName("openings")
    val openings: JsonArray? = null,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String? = null
)