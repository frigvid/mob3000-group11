package no.usn.mob3000.data.repository.social

import android.util.Log
import no.usn.mob3000.data.model.social.FriendSingleDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.social.FriendsDataSource
import no.usn.mob3000.domain.model.social.FriendData
import no.usn.mob3000.domain.repository.social.IFriendsRepository
import kotlin.math.log


/**
* @author Husseinabdulameer11
* @created  05.11.2024
**/

class FriendsRepository (
    private val authDataSource: AuthDataSource = AuthDataSource(),
    private val FriendsDataSource: FriendsDataSource = FriendsDataSource()
):IFriendsRepository{
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

    private fun FriendSingleDto.toDomainModel(): FriendData {
        return FriendData(
            friendshipId = this.friendshipId ?: "",
            friendId = this.friendId ?: "",
            displayname = this.displayName ?: "",
            eloRank = this.eloRank ?:"" ,
            avatarUrl = this.avatarUrl ?:"",
            nationality =  this.nationality ?:""
        )
    }

    }

