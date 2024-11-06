package no.usn.mob3000.domain.viewmodel



import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.domain.model.Friend
import no.usn.mob3000.domain.model.social.FriendData
import no.usn.mob3000.domain.usecase.social.FetchFriendsUseCase

/**
 * @author Husseinabdulameer11
 * @created : 01.11.2024
 */

class ProfileViewModel(
    private val fetchFriendsUseCase: FetchFriendsUseCase = FetchFriendsUseCase()
) : ViewModel() {

    private val _friends = MutableStateFlow<List<FriendData>>(emptyList())
    val friends: StateFlow<List<FriendData>> get() = _friends

    init {
        loadFriends()
    }

    private fun loadFriends() {
        viewModelScope.launch {
            fetchFriendsUseCase.fetchFriends().onSuccess {
                _friends.value = it
                Log.d("ProfileViewModel", "Loaded friends: ${it.joinToString { friend -> friend.displayname }}")
            }.onFailure { error ->
                Log.e("ProfileViewModel", "Error loading friends: ${error.message}")
            }
        }
    }
}
