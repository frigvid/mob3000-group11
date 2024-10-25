package no.usn.mob3000.data.model.game

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Serializable data transferable object representing the `public.history` table.
 *
 * NOTE: History never got implemented in the original web-application. The column names, as one
 *       can plainly see, are different compared to the other tables. And some usage is undocumented,
 *       like the score column. This is *presumably* the ELO ranking for that specific game.
 *
 * @param historyId The history item's UUID.
 * @param playedAt A timestamp representing when the game was played.
 * @param userId A UUID reference to a matching user in `auth.users`.
 * @param moves A FEN string representing the entire chess game.
 *              NOTE: FEN is more compact than PGN, but there's nothing stopping us from using that.
 * @param score The ELO score for the game.
 * @author frigvid
 * @created 2024-10-25
 */
@Serializable
data class HistoryDto(
    @SerialName("id")
    val historyId: String,
    @SerialName("datetime")
    val playedAt: Instant? = null,
    @SerialName("player")
    val userId: String? = null,
    @SerialName("fen")
    val moves: JsonObject? = null,
    @SerialName("score")
    val score: Int? = 0
)
