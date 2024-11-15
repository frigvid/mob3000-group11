package no.usn.mob3000.domain.model.social

import kotlinx.datetime.Instant

/**
 * @author Husseinabdulameer11
 * created on 05.11.2024
 **/

data class FriendData(
    val friendshipId: String,
    val friendsSince: Instant,
    val user1 : String,
    val user2 : String
    )
