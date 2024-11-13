package no.usn.mob3000.domain.viewmodel.socials

import no.usn.mob3000.domain.usecase.social.Profile.FetchUserProfileUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.repository.social.UserRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.domain.model.auth.UserProfile
import no.usn.mob3000.domain.model.social.FriendData
import no.usn.mob3000.domain.usecase.auth.GetCurrentUserIdUseCase
import no.usn.mob3000.domain.usecase.social.Profile.FetchFriendsUseCase

/**
 *
 * TODO: KDoc
 *
 * @author Husseinabdulameer11
 * @contributors 258030
 * @created 2024-11-05
 **/

class ProfileViewModel(
    private val fetchFriendsUseCase: FetchFriendsUseCase = FetchFriendsUseCase (),
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase = GetCurrentUserIdUseCase(
        authRepository = AuthRepository(
            authDataSource = AuthDataSource(),
            userDataSource = UserDataSource()
        )
    )
    ,
    private val fetchUserProfileUseCase: FetchUserProfileUseCase = FetchUserProfileUseCase(UserRepository())
) : ViewModel() {

    private val _user = MutableStateFlow<Result<UserProfile?>>(Result.success(null))
    val user: StateFlow<Result<UserProfile?>> = _user

    private val _friends = MutableStateFlow<Result<List<FriendData>>>(Result.success(emptyList()))
    val friends: StateFlow<Result<List<FriendData>>> = _friends

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    init {
        viewModelScope.launch {
            _userId.value = getCurrentUserIdUseCase.getCurrentUserId()
        }
    }

    fun fetchUser(userId: String) {
        viewModelScope.launch {
            _user.value = fetchUserProfileUseCase(userId)
        }
    }

    fun fetchFriends() {
        viewModelScope.launch {
            _friends.value = fetchFriendsUseCase.fetchFriends()
        }
    }
}






