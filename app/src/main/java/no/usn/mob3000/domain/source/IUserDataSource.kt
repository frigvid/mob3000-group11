package no.usn.mob3000.domain.source

import io.github.jan.supabase.gotrue.user.UserInfo
import no.usn.mob3000.data.model.game.GameDataDto
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.model.social.ProfileDto

/**
 * Interface for user data source.
 *
 * @author frigvid
 * @created 2024-11-07
 */
interface IUserDataSource {
    suspend fun getCurrentUser(): UserInfo
    suspend fun getUserGameStats(): GameDataDto
    suspend fun getUserProfile(userId: String): ProfileDto?
    suspend fun getUserFriends(userId: String): List<FriendsDto>
    suspend fun getUserFriendRequests(): List<FriendRequestsDto>
}
