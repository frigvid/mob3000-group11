package no.usn.mob3000.domain.model.social

import kotlinx.datetime.Instant

/**
 * @author Husseinabdulameer11
 * @created on 2024-11-05
 **/
data class FriendData(
    val friendshipId: String,
    val friendsSince: Instant,
    val user1: String,
    val user2: String
)
