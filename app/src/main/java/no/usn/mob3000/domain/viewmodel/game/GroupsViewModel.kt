package no.usn.mob3000.domain.viewmodel.game

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import no.usn.mob3000.data.repository.game.GroupsRepository
import no.usn.mob3000.domain.model.game.Group
import no.usn.mob3000.domain.model.game.state.GroupState
import no.usn.mob3000.domain.usecase.game.group.CreateGroupUseCase
import no.usn.mob3000.domain.usecase.game.group.DeleteGroupUseCase
import no.usn.mob3000.domain.usecase.game.group.FetchGroupSingleUseCase
import no.usn.mob3000.domain.usecase.game.group.FetchGroupsUseCase
import no.usn.mob3000.domain.usecase.game.group.UpdateGroupUseCase
import no.usn.mob3000.domain.viewmodel.auth.AuthenticationViewModel

/**
 * ViewModel to track state for repertoires/groups.
 *
 * @author frigvid
 * @created 2024-11-15
 */
class GroupsViewModel(
    private val authenticationViewModel: AuthenticationViewModel = AuthenticationViewModel(),
    private val createGroupUseCase: CreateGroupUseCase = CreateGroupUseCase(GroupsRepository()),
    private val fetchGroupsUseCase: FetchGroupsUseCase = FetchGroupsUseCase(GroupsRepository()),
    private val fetchGroupSingleUseCase: FetchGroupSingleUseCase = FetchGroupSingleUseCase(GroupsRepository()),
    private val updateGroupUseCase: UpdateGroupUseCase = UpdateGroupUseCase(GroupsRepository()),
    private val deleteGroupUseCase: DeleteGroupUseCase = DeleteGroupUseCase(GroupsRepository())
) : ViewModel() {
    private val _groupState = MutableStateFlow<GroupState>(GroupState.Idle)
    val groupState = _groupState.asStateFlow()

    private val _groups = mutableStateOf<List<Group>>(emptyList())
    val groups: State<List<Group>> = _groups

    private val _groupSingle = mutableStateOf<Group?>(null)
    val groupSingle: State<Group?> = _groupSingle

    private val _selectedGroup = mutableStateOf<Group?>(null)
    val selectedGroup: State<Group?> = _selectedGroup

    private var periodicUpdateJob: Job? = null
    private val stateMutex = Mutex()

    /**
     * Function to periodically call [fetchGroups]. This also
     * stops any existing jobs, in case there's any dirty state.
     *
     * @author frigvid
     * @created 2024-11-15
     */
    fun startPeriodicUpdates() {
        Log.d(TAG, "Starting periodic update by first canceling pending jobs.")
        stopPeriodicUpdates()

        Log.d(TAG, "Starting job!")
        periodicUpdateJob = viewModelScope.launch {
            while (isActive) {
                Log.d(TAG, "Fetching groups.")
                fetchGroups()

                Log.d(TAG, "Updating authentication state.")
                authenticationViewModel.updateAuthState()

                Log.d(TAG, "Delaying for 5 minutes.")
                delay(REFRESH_INTERVAL)
            }
        }
    }

    /**
     * Function to clean up job state by stopping any pending
     * jobs and removing any stored reference to it.
     *
     * @author frigvid
     * @created 2024-11-15
     */
    private fun stopPeriodicUpdates() {
        periodicUpdateJob?.cancel()
        periodicUpdateJob = null
    }

    /**
     * Clean up the repertoire/group state, stop pending jobs, and
     * reset to its default state after.
     *
     * @author frigvid
     * @created 2024-11-15
     */
    suspend fun resetGroupsState() {
        stateMutex.withLock {
            stopPeriodicUpdates()
            _groupState.emit(GroupState.Idle)
            Log.w(TAG, "Resetting groups state! ${_groupState.value}")
        }
    }

    /**
     * Override super implementation of onCleared to additionally also
     * stop any pending jobs and related states, when clearing the view
     * model.
     *
     * @author frigvid
     * @created 2024-11-15
     */
    override fun onCleared() {
        super.onCleared()
        stopPeriodicUpdates()
    }

    /**
     * This is the main handler for fetching repertoires/groups and updating the group state.
     *
     * @author frigvid
     * @created 2024-11-15
     */
    fun fetchGroups() {
        viewModelScope.launch {
            stateMutex.withLock {
                _groupState.emit(GroupState.Loading)

                try {
                    fetchGroupsUseCase().fold(
                        onSuccess = { groupsList ->
                            _groups.value = groupsList
                            _groupState.emit(GroupState.Success)
                        },
                        onFailure = { error ->
                            Log.e(TAG, "Failure! Something went wrong while fetching groups!", error)
                            _groupState.emit(GroupState.Error(Exception(error)))
                        }
                    )
                } catch (error: Exception) {
                    Log.e(TAG, "Something unknown went wrong!", error)
                    _groupState.emit(GroupState.Error(error))
                }
            }
        }
    }

    /**
     * Fetches a single [Group].
     *
     * @param groupId The ID of the [Group] to fetch.
     * @author frigvid
     * @created 2024-11-15
     */
    fun fetchGroupSingle(
        groupId: String
    ) {
        viewModelScope.launch {
            try {
                fetchGroupSingleUseCase(groupId).fold(
                    onSuccess = { group ->
                        _groupSingle.value = group
                    },
                    onFailure = { error ->
                        Log.e(TAG, "Something went wrong while fetching the group with ID: $groupId", error)
                    }
                )
            } catch (error: Exception) {
                Log.e(TAG, "Something unknown went wrong when fetching a single (1) group!", error)
            }
        }
    }

    /**
     * Creates a repertoire/group.
     *
     * @param group The [Group] object to be created.
     * @author frigvid
     * @created 2024-11-15
     */
    fun createGroup(
        group: Group
    ) {
        viewModelScope.launch {
            try {
                createGroupUseCase(group)
            } catch (error: Exception) {
                Log.e(TAG, "Something unknown went wrong while creating the group!", error)
            }
        }
    }

    /**
     * Deletes a repertoire/group.
     *
     * @param groupId The ID of the [Group] to be deleted.
     * @author frigvid
     * @created 2024-11-15
     */
    fun deleteGroup(
        groupId: String
    ) {
        viewModelScope.launch {
            try {
                Log.d("GroupsViewModel", "Deleting group with ID: $groupId")
                deleteGroupUseCase(groupId)
            } catch (error: Exception) {
                Log.e(TAG, "Something unknown went wrong while deleting the group!", error)
            }
        }
    }

    /**
     * Updates a repertoire/group.
     *
     * @param group The [Group] object to be updated.
     * @author frigvid
     * @created 2024-11-15
     */
    fun updateGroup(
        group: Group
    ) {
        viewModelScope.launch {
            try {
                updateGroupUseCase(group)
            } catch (error: Exception) {
                Log.e(TAG, "Something went wrong while updating the group!", error)
            }
        }
    }

    fun setGroups(groups: List<Group>) {
        _groups.value = groups
    }

    fun setSelectedGroup(group: Group) {
        _selectedGroup.value = group
    }

    /**
     * Retrieves a stored group by its ID.
     *
     * @param id The ID of the [Group] to retrieve.
     * @return The [Group] with the matching ID, or null if not found.
     * @author frigvid
     * @created 2024-11-15
     */
    fun getGroupById(id: String): Group? {
        return _groups.value.find { it.id == id }
    }

    companion object {
        /**
         * Represents 5 minutes.
         */
        private const val REFRESH_INTERVAL = 5 * 60 * 1000L
        private const val TAG = "GroupsViewModel"
    }
}
