package no.usn.mob3000.domain.viewmodel.socials

import android.util.Log
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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import no.usn.mob3000.data.repository.social.ProfileEditRepository
import no.usn.mob3000.domain.model.auth.UserGameStats
import no.usn.mob3000.domain.model.social.FriendRequestData
import no.usn.mob3000.domain.usecase.auth.GetCurrentUserIdUseCase
import no.usn.mob3000.domain.usecase.social.requests.FriendRequestUseCase
import no.usn.mob3000.domain.usecase.social.userProfile.FetchFriendsUseCase
import no.usn.mob3000.domain.usecase.social.userProfile.FetchUserByIdUseCase
import no.usn.mob3000.domain.usecase.social.userProfile.FetchUserProfileUseCase
import no.usn.mob3000.domain.usecase.social.userProfile.GetUserGameStatsUseCase
import no.usn.mob3000.domain.usecase.social.userUpdate.UpdateProfileUseCase
/**
 * ViewModel for handling user profile-related functionality. Using the different usecases to communicate with the business logic handled in
 * the data layer
 *
 * @param updateProfileUseCase The use case for updating a user's profile.
 * @param fetchFriendsUseCase The use case for fetching a user's friends.
 * @param friendRequestUseCase The use case for managing friend requests.
 * @param fetchUserByIdUseCase The use case for fetching a user by their ID.
 * @param getCurrentUserIdUseCase The use case for getting the current user's ID.
 * @param fetchUserProfileUseCase The use case for fetching a user's profile.
 * @author Husseinabdulameer11, 258030
 * @created 2024-11-05
 **/
class ProfileViewModel(
    private val updateProfileUseCase: UpdateProfileUseCase = UpdateProfileUseCase(ProfileEditRepository()),
    private val fetchFriendsUseCase: FetchFriendsUseCase = FetchFriendsUseCase(),
    private val friendRequestUseCase: FriendRequestUseCase = FriendRequestUseCase(),
    private val fetchUserByIdUseCase: FetchUserByIdUseCase = FetchUserByIdUseCase(UserRepository()),
    private val getUserGameStatsUseCase: GetUserGameStatsUseCase = GetUserGameStatsUseCase(),
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase = GetCurrentUserIdUseCase(
        authRepository = AuthRepository(
            authDataSource = AuthDataSource(),
            userDataSource = UserDataSource()
        )
    ),
    private val fetchUserProfileUseCase: FetchUserProfileUseCase = FetchUserProfileUseCase(UserRepository())
) : ViewModel() {
    /* User profiles. */
    private val _userProfiles = MutableStateFlow<Map<String, UserProfile>>(emptyMap())
    val userProfiles: StateFlow<Map<String, UserProfile>> = _userProfiles

    private val _currentUserProfile = MutableStateFlow<UserProfile?>(null)
    val currentUserProfile: StateFlow<UserProfile?> = _currentUserProfile

    private val _userGameStats = MutableStateFlow<Result<UserGameStats>>(Result.success(UserGameStats(0, 0, 0)))
    val userGameStats: StateFlow<Result<UserGameStats>> = _userGameStats

    /* ID/Selection */
    private val _selectedUser = mutableStateOf<UserProfile?>(null)
    val selectedUser: State<UserProfile?> = _selectedUser

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId

    /* Friend requests. */
    private val _friends = MutableStateFlow<Result<List<FriendData>>>(Result.success(emptyList()))
    val friends: StateFlow<Result<List<FriendData>>> = _friends

    private val _nonFriends = MutableStateFlow<Result<List<UserProfile>>>(Result.success(emptyList()))
    val nonFriends: StateFlow<Result<List<UserProfile>>> = _nonFriends

    private val _friendRequests = MutableStateFlow<Result<List<FriendRequestData>>>(Result.success(emptyList()))
    val friendRequests: StateFlow<Result<List<FriendRequestData>>> = _friendRequests

    /**
     * Initializes the ViewModel by fetching the current user's ID and fetching their profile. Used primary to ensure better refreshes, as this part of the system will not
     * have access to the room database.
     */
    init {
        viewModelScope.launch {
            _userId.value = getCurrentUserIdUseCase.getCurrentUserId()
            userId.value?.let { userId ->
                fetchUser(userId)
                fetchUserGameStats()
            }
        }
    }

    /***************Friend request***************/
    /**
     * Inserts a friend request into the database.
     *
     * @param toUser The ID of the user to send the friend request to.
     */
    fun insertFriendRequest(
        toUser: String
    ) {
        viewModelScope.launch {
            friendRequestUseCase.insert(toUser)
        }
    }
    /**
     * Accepts a friend request.
     */
    fun acceptFriendRequest(friendRequestId: String) {
        viewModelScope.launch {
            val result = friendRequestUseCase.accept(friendRequestId)
            result.onSuccess {
                fetchFriendRequests()
                fetchFriends()
            }.onFailure {
                Log.e("ProfileViewModel", "Error when accepting: ${it.message}")
            }
        }
    }
    /**
     * Declines a friend request.
     */
    fun declineFriendRequest(friendRequestId: String) {
        viewModelScope.launch {
            val result = friendRequestUseCase.decline(friendRequestId)
            result.onSuccess {
                fetchFriendRequests()
            }.onFailure {
                Log.e("ProfileViewModel", "Error when declining: ${it.message}")
            }
        }
    }
    /***************User profile***************/
    /**
     * Fetches a user's profile from the database.
     */
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
    /**
     * Fetches a user's game stats from the database.
     */
    fun fetchUserGameStats() {
        viewModelScope.launch {
            val result = getUserGameStatsUseCase.invoke()
            result.onSuccess { userGameStats ->
                _userGameStats.value = result
            }.onFailure {
                Log.e("ProfileViewModel", "Failed to fetch user game stats: ${it.message}")
            }
        }
    }
    /**
     * Fetches a user by their ID from the database.
     */
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
    /**
     * Sets the selected user for further actions.
     */
    fun setSelectedUser(selectedUser: UserProfile) {
        _selectedUser.value = selectedUser
    }

    /***************Friends**************/
    /**
     * Fetches a user's friends from the database.
     */
    fun fetchFriends() {
        viewModelScope.launch {
            _userId.value?.let { userId ->
                val result = fetchFriendsUseCase.fetchFriends(userId)
                _friends.value = result
            }
        }
    }
    /**
     * Fetches a user's non-friends from the database.
     */
    fun fetchNonFriends() {
        viewModelScope.launch {
            _userId.value?.let { userId ->
                val result = fetchFriendsUseCase.fetchNonFriends(userId)
                _nonFriends.value = result
            }
        }
    }
    /**
     * Fetches a user's friend requests from the database.
     */
    fun fetchFriendRequests() {
        viewModelScope.launch {
            val result = fetchFriendsUseCase.fetchFriendRequests()
            _friendRequests.value = result
        }
    }

    /***************Update**************/
    /**
     * Saves changes made to the currently selected user's profile.
     *
     * @param displayName The new display name for the user.
     * @param avatarUrl The new avatar URL for the user.
     * @param aboutMe The new about-me text for the user.
     * @param nationality The new nationality for the user.
     * @param profileVisibility Whether the user's profile is visible or not.
     * @param friendsVisibility Whether the user's friends list is visible or not.
     */
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
     *
     * @param userid The ID of the user to update.
     * @param displayName The new display name for the user.
     * @param avatarUrl The new avatar URL for the user.
     * @param aboutMe The new about-me text for the user.
     * @param nationality The new nationality for the user.
     * @param profileVisibility Whether the user's profile is visible or not.
     * @param friendsVisibility Whether the user's friends list is visible or not.
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







