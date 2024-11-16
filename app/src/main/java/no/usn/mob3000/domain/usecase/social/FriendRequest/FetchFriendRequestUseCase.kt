package no.usn.mob3000.domain.usecase.social.FriendRequest

import no.usn.mob3000.data.repository.social.FriendRequestRepository
import no.usn.mob3000.domain.model.social.FriendRequestData

class FetchFriendRequestUseCase (
  private val fetchRepository: FriendRequestRepository = FriendRequestRepository()
){
    suspend fun execute(): Result<List<FriendRequestData>>{
        return fetchRepository.fetchFriendRequests()
    }
}
