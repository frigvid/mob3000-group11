package no.usn.mob3000.domain.viewmodel.socials

import android.util.Log
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
import no.usn.mob3000.domain.usecase.social.Profile.FetchFriendsUseCase
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import no.usn.mob3000.data.repository.social.ProfileEditRepository
import no.usn.mob3000.domain.model.social.FriendRequestData
import no.usn.mob3000.domain.usecase.auth.GetCurrentUserIdUseCase
import no.usn.mob3000.domain.usecase.social.AddFriends.FetchNonFriendsUseCase
import no.usn.mob3000.domain.usecase.social.FriendRequest.AcceptFriendRequestUseCase
import no.usn.mob3000.domain.usecase.social.FriendRequest.FetchFriendRequestUseCase
import no.usn.mob3000.domain.usecase.social.FriendRequest.InsertFriendRequestUseCase
import no.usn.mob3000.domain.usecase.social.Profile.FetchUserByIdUseCase
import no.usn.mob3000.domain.usecase.social.ProfileEdit.UpdateProfileUseCase

/**
 *
 * TODO: KDoc
 *
 * @author Husseinabdulameer11
 * @contributors 258030
 * @created 2024-11-05
 **/

class ProfileViewModel(
    private val updateProfileUseCase: UpdateProfileUseCase = UpdateProfileUseCase(ProfileEditRepository()),
    private val insertFriendRequestUseCase: InsertFriendRequestUseCase = InsertFriendRequestUseCase(),
    private val fetchFriendsUseCase: FetchFriendsUseCase = FetchFriendsUseCase(),
    private val fetchFriendRequestUseCase: FetchFriendRequestUseCase = FetchFriendRequestUseCase(),
    private val fetchNonFriendsUseCase: FetchNonFriendsUseCase = FetchNonFriendsUseCase(),
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase = AcceptFriendRequestUseCase(),
    private val fetchUserByIdUseCase: FetchUserByIdUseCase = FetchUserByIdUseCase(UserRepository()),
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase = GetCurrentUserIdUseCase(
        authRepository = AuthRepository(
            authDataSource = AuthDataSource(),
            userDataSource = UserDataSource()
        )
    ),
    private val fetchUserProfileUseCase: FetchUserProfileUseCase = FetchUserProfileUseCase(UserRepository())
) : ViewModel() {

    private val _userProfiles = MutableStateFlow<Map<String, UserProfile>>(emptyMap())
    val userProfiles: StateFlow<Map<String, UserProfile>> = _userProfiles

    private val _selectedUser = mutableStateOf<UserProfile?>(null)
    val selectedUser: State<UserProfile?> = _selectedUser

    private val _friends = MutableStateFlow<Result<List<FriendData>>>(Result.success(emptyList()))
    val friends: StateFlow<Result<List<FriendData>>> = _friends

    private val _nonFriends = MutableStateFlow<Result<List<UserProfile>>>(Result.success(emptyList()))
    val nonFriends: StateFlow<Result<List<UserProfile>>> = _nonFriends

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    private val _friendRequests = MutableStateFlow<Result<List<FriendRequestData>>>(Result.success(emptyList()))
    val friendRequests: StateFlow<Result<List<FriendRequestData>>> = _friendRequests

    private val _currentUserProfile = MutableStateFlow<UserProfile?>(null)
    val currentUserProfile: StateFlow<UserProfile?> = _currentUserProfile

    init {
        viewModelScope.launch {
            _userId.value = getCurrentUserIdUseCase.getCurrentUserId()
            userId.value?.let { userId ->
                fetchUser(userId)
            }
        }
    }

    fun insertFriendRequest(
        toUser: String
    ) {
        viewModelScope.launch {
            insertFriendRequestUseCase.execute(toUser)
        }
    }

    fun acceptFriendRequest(friendRequestId: String) {
        viewModelScope.launch {
            val result = acceptFriendRequestUseCase.accept(friendRequestId)
            result.onSuccess {
                fetchFriendRequests()
                fetchFriends()
            }.onFailure {
                Log.e("ProfileViewModel", "Error when accepting: ${it.message}")
            }
        }
    }

    fun declineFriendRequest(friendRequestId: String) {
        viewModelScope.launch {
            val result = acceptFriendRequestUseCase.decline(friendRequestId)
            result.onSuccess {
                fetchFriendRequests()
            }.onFailure {
                Log.e("ProfileViewModel", "Error when declining: ${it.message}")
            }
        }
    }


    fun fetchUser(userId: String) {
        viewModelScope.launch {
            val result = fetchUserProfileUseCase(userId)
            result.onSuccess { userProfile ->
                _currentUserProfile.value = userProfile
            }.onFailure {
                Log.e("ProfileViewModel", "Failed to fetch user profile: ${it.message}")
            }
        }
    }

    fun fetchUserById(userId: String) {
        viewModelScope.launch {
            val result = fetchUserByIdUseCase(userId)
            result.onSuccess { userProfile ->
                userProfile?.let {
                    _userProfiles.value = _userProfiles.value + (userId to it)
                }
            }
        }
    }

    fun fetchFriends() {
        viewModelScope.launch {
            _friends.value = fetchFriendsUseCase.fetchFriends()
        }
    }

    fun fetchNonFriends() {
        viewModelScope.launch {
            _userId.value?.let { userId ->
                val result = fetchNonFriendsUseCase.execute(userId)
                _nonFriends.value = result
            }
        }
    }

    fun fetchFriendRequests() {
        viewModelScope.launch {
            val result = fetchFriendRequestUseCase.execute()
            _friendRequests.value = result
        }
    }


    fun setSelectedUser(selectedUser: UserProfile) {
        _selectedUser.value = selectedUser
    }

    fun saveProfileChanges(
        displayName: String,
        avatarUrl: String,
        aboutMe: String,
        nationality: String,
        profileVisibility: Boolean,
        friendsVisibility: Boolean
    ) {
        _selectedUser.value?.let { profiles ->
            updateUserInDb(
                userid = profiles.userId,
                displayName = displayName,
                avatarUrl = avatarUrl,
                aboutMe = aboutMe,
                nationality = nationality,
                profileVisibility = profileVisibility,
                friendsVisibility = friendsVisibility
            )
        }
    }

    /**
     * Updates the user's profile in the database.
     */
    private fun updateUserInDb(
        userid: String,
        displayName: String,
        avatarUrl: String,
        aboutMe: String,
        nationality: String,
        profileVisibility: Boolean,
        friendsVisibility: Boolean
    ) {
        viewModelScope.launch {
            updateProfileUseCase.execute(
                userid = userid,
                displayName = displayName,
                avatarUrl = avatarUrl,
                aboutMe = aboutMe,
                nationality = nationality,
                profileVisibility = profileVisibility,
                friendsVisibility = friendsVisibility
            )
        }
    }



}







