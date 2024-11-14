package no.usn.mob3000.domain.usecase.social.Profile


import no.usn.mob3000.data.repository.social.FriendsRepository
import no.usn.mob3000.domain.model.social.FriendData


class GetFriendProfileUseCase(
    private val friendsRepository: FriendsRepository = FriendsRepository()
){

    suspend fun getFriendProfile(userId: String):Result<List<FriendData>> {
        return friendsRepository.getFriendProfile(userId)
    }
}
