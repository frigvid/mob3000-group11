package no.usn.mob3000.domain.source

import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.gotrue.user.UserSession
import no.usn.mob3000.data.model.game.GameDataDto
import no.usn.mob3000.data.model.social.FriendRequestsDto
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.model.social.ProfileDto

/**
 * Interface for authentication data source.
 *
 * @author frigvid
 * @created 2024-10-28
 */
interface IAuthDataSource {
    suspend fun getCurrentSession(): UserSession?
    suspend fun getCurrentUser(): UserInfo
    suspend fun getUserGameStats(): GameDataDto
    suspend fun getUserProfile(userId: String): ProfileDto?
    suspend fun getUserFriends(userId: String): List<FriendsDto>
    suspend fun getUserFriendRequests(): List<FriendRequestsDto>
}
