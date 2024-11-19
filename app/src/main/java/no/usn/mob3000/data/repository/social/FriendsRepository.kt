package no.usn.mob3000.data.repository.social

import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.source.remote.social.FriendRequestDataSource
import no.usn.mob3000.data.source.remote.social.FriendsDataSource
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.model.social.FriendData
import no.usn.mob3000.domain.repository.social.IFriendsRepository

/**
 *
 * This repository orchestrates mainly the showcasing of the users friends. In difference to [FriendRequestRepository], this repository
 * contains only fetch-actions.
 *
 * Mapping is implemented to join [displayName] from [UserProfile] to [FriendData].
 * TODO: Deletion of a friendship and other actions if we have the time
 *
 * @param FriendsDataSource The data source for fetching friends.
 * @param profileUserDataSource The data source for fetching non-friend profiles.
 * @author Husseinabdulameer11, 258030
 * @created 2024-11-05
 **/
class FriendsRepository (
    private val friendsDataSource: FriendsDataSource = FriendsDataSource(),
    private val friendRequestDataSource: FriendRequestDataSource = FriendRequestDataSource(),
) : IFriendsRepository {
    /**
     *
     * Fetches all friends and puts it into a list, maps it for usage in the ui layer. Checks for if the user ID
     * matches a column, shows the row where the userId matched least one of the columns (either user1 or user2).
     * More filtration is done in the ui layer.
     *
     * @param userId The user id of the current user. Used to filter out friend connections that does not exist
     * @return A list of [FriendData]
     * @author 258030
     * @created 2024-11-14
     */
    override suspend fun fetchFriends(userId: String): Result<List<FriendData>> {
        return try{
            val friendsList = friendsDataSource.fetchFriends()
            val userFriends = friendsList.filter { it.user1 == userId || it.user2 == userId }
            Result.success(userFriends.map { it.toDomainModel() })
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    /**
     *
     * Fetches all non-friends and maps it for usage in the ui layer. Used for adding new friends
     * It also checks for if the userId matches byUser and the targeted userId matches toUser, that way we don't get duplicate friend requests
     *
     * @param userId The user id of the current user. Used to filter out friend connections that does not exist
     * @author 258030
     * @created 2024-11-15
     */
    override suspend fun fetchNonFriends(userId: String): Result<List<UserProfile>> {
        return try {
            val friendIds = friendsDataSource.fetchFriends()
                .flatMap { listOf(it.user1, it.user2) }
                .filter { it != userId }
            val friendRequestIds = friendRequestDataSource.fetchAllFriendRequests(userId)
                .flatMap { listOf(it.byUser, it.toUser) }
                .filter { it != userId }
            val excludedIds = friendIds + friendRequestIds
            val nonFriendProfiles = friendsDataSource.fetchNonFriends(userId)
                .filter { it.userId !in excludedIds }
            val nonFriendProfilesMapped = nonFriendProfiles.map { it.toDomainModel() }
            Result.success(nonFriendProfilesMapped)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     *
     * Maps [FriendsDto] to [FriendData]
     *
     * @return The corresponding [FriendData] instance
     */
    private fun FriendsDto.toDomainModel(): FriendData {
        return FriendData(
            friendshipId = this.friendshipId ?: "",
            friendsSince = this.friendsSince,
            user1 = this.user1 ?:"",
            user2 = this.user2 ?: ""
        )
    }

    /**
     *
     * Maps [ProfileDto] to [UserProfile]
     *
     * @return The corresponding [UserProfile] instance
     */
    private fun ProfileDto.toDomainModel(): UserProfile {
        return UserProfile(
            userId = this.userId,
            displayName = this.displayName?: "",
            eloRank = this.eloRank?: 0,
            avatarUrl = this.avatarUrl?: "",
            aboutMe = this.aboutMe?: "",
            nationality = this.nationality?: "",
            visibility = this.profileVisibility,
            visibilityFriends = this.friendsVisibility
        )
    }
}
