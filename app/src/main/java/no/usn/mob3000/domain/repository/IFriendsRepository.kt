package no.usn.mob3000.domain.repository

import no.usn.mob3000.data.model.social.FriendSingleDto
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.domain.model.Friend

/**
 * @author Hussein Abdul-Ameer
 * created on: 02.11.2024
 */
// In IFriendsRepository
// In IFriendsRepository
interface IFriendsRepository {
    suspend fun fetchFriends(): Result<List<Friend>>
}
