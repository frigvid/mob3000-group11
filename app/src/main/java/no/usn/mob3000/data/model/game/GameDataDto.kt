package no.usn.mob3000.data.model.game

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable data transferable object representing the `public.gamedata` table.
 *
 * @param userId A UUID reference to a matching user in `auth.users`.
 * @param gameWins An integer describing how many times the user has won.
 * @param gameLosses An integer describing how many times the user has lost.
 * @param gameDraws An integer describing how many times the user has ended up in a draw.
 * @author frigvid
 * @created 2024-10-25
 */
@Serializable
data class GameDataDto(
    @SerialName("id")
    val userId: String,
    @SerialName("wins")
    val gameWins: Int? = 0,
    @SerialName("losses")
    val gameLosses: Int? = 0,
    @SerialName("draws")
    val gameDraws: Int? = 0
)