package no.usn.mob3000.domain.model.social

/**
* @author Husseinabdulameer11
* created on 05.11.2024
**/

data class FriendData(
    val friendshipId: String,
    val friendId: String,
    val displayname: String,
    val eloRank: Comparable<*>,
    val avatarUrl: String,
    val nationality: String,

    )
