package no.usn.mob3000.domain.model.social
/**
* @author Hussein Abdul-Ameer
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
