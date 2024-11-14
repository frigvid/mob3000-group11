package no.usn.mob3000.data.repository.social

import android.util.Log
import no.usn.mob3000.data.model.social.FriendSingleDto
import no.usn.mob3000.data.model.social.FriendsDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.social.FriendsDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.domain.model.social.FriendData
import no.usn.mob3000.domain.repository.social.IFriendsRepository
import no.usn.mob3000.ui.screens.user.friendComponent


/**
 * @author Husseinabdulameer11
 * @created  05.11.2024
 **/

class FriendsRepository (

    private val FriendsDataSource: FriendsDataSource = FriendsDataSource(),
    private val userDataSource : UserDataSource = UserDataSource()
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

    private fun FriendsDto.toDomainModel(): FriendData {
        return FriendData(
            friendshipId = this.friendshipId ?: "",
            friendsSince = this.friendsSince,
            user1 = this.user1 ?:"",
            user2 = this.user2 ?: ""
        )
    }

}
