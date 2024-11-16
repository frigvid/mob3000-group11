package no.usn.mob3000.domain.model.social

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName

data class FriendRequestData(
    val friendRequestId: String,
    val createdAt: Instant,
    val byUser: String,
    val toUser: String,
    val accepted: Boolean,
    val displayName: String? = null
)

