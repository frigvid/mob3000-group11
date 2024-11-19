package no.usn.mob3000.domain.model.social

import kotlinx.datetime.Instant

/**
 * The pure kotlin data class representing a friendship.
 *
 * @author Husseinabdulameer11
 * @created 2024-11-05
 */
data class FriendData(
    val friendshipId: String,
    val friendsSince: Instant,
    val user1: String,
    val user2: String
)
