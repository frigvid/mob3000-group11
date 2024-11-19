package no.usn.mob3000.domain.model.social

import kotlinx.datetime.Instant

/**
 * The pure Kotlin data class representing a friend request.
 *
 * @author Husseinabdulameer11
 * @created 2024-11-05
 */
data class FriendRequestData(
    val friendRequestId: String,
    val createdAt: Instant,
    val byUser: String,
    val toUser: String,
    val accepted: Boolean,
    val displayName: String? = null
)
