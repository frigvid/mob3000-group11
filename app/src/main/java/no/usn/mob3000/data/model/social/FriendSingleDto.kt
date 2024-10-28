package no.usn.mob3000.data.model.social

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable data transferable object representing the composed return table from the RPC
 * function `public.friend_get_one`.
 *
 * @param friendshipId The friendship item UUID.
 * @param friendId A UUID reference matching a row in `auth.users`.
 * @param displayName The friend's display name.
 * @param eloRank The friend's elo rank.
 * @param avatarUrl The friend's profile picture.
 * @param nationality The friend's nationality.
 * @author frigvid
 * @created 2024-10-25
 */
@Serializable
data class FriendSingleDto(
    @SerialName("friendship_id")
    val friendshipId: String,
    @SerialName("id")
    val friendId: String,
    @SerialName("display_name")
    val displayName: String? = "",
    @SerialName("elo_rank")
    val eloRank: Int? = 0,
    @SerialName("avatar_ur")
    val avatarUrl: String? = "",
    @SerialName("nationality")
    val nationality: String? = ""
)
