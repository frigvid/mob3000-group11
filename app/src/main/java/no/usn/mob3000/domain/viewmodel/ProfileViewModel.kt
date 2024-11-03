package no.usn.mob3000.domain.viewmodel

/**
 * @author Hussein Abdul-Ameer
 * created on : 01.11.2024
 */
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.domain.model.Friend
import no.usn.mob3000.domain.usecase.social.FetchFriendsUseCase

class ProfileViewModel(
    private val fetchFriendsUseCase: FetchFriendsUseCase = FetchFriendsUseCase()
) : ViewModel() {

    private val _friends = MutableStateFlow<List<Friend>>(emptyList())
    val friends: StateFlow<List<Friend>> get() = _friends

    init {
        loadFriends()
    }

    private fun loadFriends() {
        viewModelScope.launch {
            fetchFriendsUseCase.fetchFriends().onSuccess {
                _friends.value = it
                Log.d("ProfileViewModel", "Loaded friends: ${it.joinToString { friend -> friend.displayName }}")
            }.onFailure { error ->
                Log.e("ProfileViewModel", "Error loading friends: ${error.message}")
            }
        }
    }
}
