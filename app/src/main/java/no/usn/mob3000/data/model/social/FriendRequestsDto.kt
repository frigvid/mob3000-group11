package no.usn.mob3000.data.model.social

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable data transferable object representing the `public.friend_requests` table.
 *
 * @param friendRequestId The friend request item UUID.
 * @param createdAt A timestamp representing when the friend request item was created.
 * @param byUser A UUID reference to a matching row in `auth.users` for the user sending the request.
 * @param toUser A UUID reference to a matching row in `auth.users` for the user receiving the request.
 * @param accepted A boolean switch representing whether the friend request has been accepted yet.
 *                 NOTE: This is managed by database functions and triggers and should not be
 *                       manually handled.
 * @author frigvid
 * @created 2024-10-25
 */
@Serializable
data class FriendRequestsDto(
    @SerialName("id")
    val friendRequestId: String,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("by_user")
    val byUser: String? = null,
    @SerialName("to_user")
    val toUser: String? = null,
    @SerialName("accepted")
    val accepted: Boolean? = false
)