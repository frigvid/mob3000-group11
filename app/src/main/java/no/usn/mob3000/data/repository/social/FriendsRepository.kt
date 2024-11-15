package no.usn.mob3000.data.repository.social

import android.util.Log
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.model.social.ProfileDto
import no.usn.mob3000.data.source.remote.social.FriendsDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.data.source.remote.social.ProfileUserDataSource
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.model.social.FriendData
import no.usn.mob3000.domain.repository.social.IFriendsRepository

/**
 * @author Husseinabdulameer11
 * @created  05.11.2024
 **/

class FriendsRepository (

    private val FriendsDataSource: FriendsDataSource = FriendsDataSource(),
    private val userDataSource : UserDataSource = UserDataSource(),
    private val profileUserDataSource: ProfileUserDataSource = ProfileUserDataSource()
):IFriendsRepository{

   override suspend fun getFriendProfile(userId: String):Result<List<FriendData>>{
       return try {
           val friendprofileDto = userDataSource.getUserFriends(userId);
           Result.success( friendprofileDto.map { it.toDomainModel() })
       }
       catch (e: Exception){
           Result.failure(e)
       }
   }

    override suspend fun FetchFriends(): Result<List<FriendData>> {
        return try{
            val Friendslist = FriendsDataSource.fetchAllFriends();
            Friendslist.forEach { friend ->
                Log.d("FriendsRepository", "Friend: $friend")
            }
            Result.success(Friendslist.map {it.toDomainModel()  })
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun fetchNonFriends(userId: String): Result<List<UserProfile>> {
        return try {
            val nonFriendProfiles: List<ProfileDto> = profileUserDataSource.fetchNonFriends(userId)
            val nonFriendProfilesMapped = nonFriendProfiles.map { it.toDomainModel() }
            Result.success(nonFriendProfilesMapped)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    private fun FriendsDto.toDomainModel(): FriendData {
        return FriendData(
            friendshipId = this.friendshipId ?: "",
            friendsSince = this.friendsSince,
            user1 = this.user1 ?:"",
            user2 = this.user2 ?: ""
        )
    }

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


