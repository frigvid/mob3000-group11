package no.usn.mob3000.domain.viewmodel.socials

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.domain.model.social.FriendData
import no.usn.mob3000.domain.usecase.social.FetchFriendsUseCase

class ProfileViewModel (
    private val fetchFriendsUseCase: FetchFriendsUseCase = FetchFriendsUseCase()
):ViewModel(){
private val _friends = MutableStateFlow<Result<List<FriendData>>>(Result.success(emptyList()))
    val friends : StateFlow<Result<List<FriendData>>> = _friends
    private val _selectedFriend = mutableStateOf<FriendData?>(null)
   val selectedFriend: State<FriendData?> = _selectedFriend
    fun fetchFriends() {
        viewModelScope.launch {
            _friends.value = fetchFriendsUseCase.fetchFriends()
        }
    }
}
