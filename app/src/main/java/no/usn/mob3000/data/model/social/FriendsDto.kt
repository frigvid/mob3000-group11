package no.usn.mob3000.data.model.social

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable data transferable object representing the `public.friends` table.
 *
 * @param friendshipId The friendship item UUID.
 * @param friendsSince A timestamp representing when the friendship was created.
 * @param user1 A UUID reference matching a row in `auth.users`.
 * @param user2 A UUID reference matching a row in `auth.users`.
 * @author frigvid
 * @created 2024-10-25
 */
@Serializable
data class FriendsDto(
    @SerialName("id")
    val friendshipId: String,
    @SerialName("friends_since")
    val friendsSince: Instant,
    @SerialName("user1")
    val user1: String,
    @SerialName("user2")
    val user2: String
)
