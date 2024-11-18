package no.usn.mob3000.domain.model.social

import kotlinx.datetime.Instant


/**
 * @author Husseinabdulameer11
 * created on 05.11.2024
 **/
data class FriendRequestData(
    val friendRequestId: String,
    val createdAt: Instant,
    val byUser: String,
    val toUser: String,
    val accepted: Boolean,
    val displayName: String? = null
)

