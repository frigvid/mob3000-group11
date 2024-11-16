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
import no.usn.mob3000.data.repository.game.OpeningsRepository
import no.usn.mob3000.domain.model.game.opening.Opening
import no.usn.mob3000.domain.model.game.opening.OpeningState
import no.usn.mob3000.domain.usecase.game.opening.CreateOpeningUseCase
import no.usn.mob3000.domain.usecase.game.opening.DeleteOpeningUseCase
import no.usn.mob3000.domain.usecase.game.opening.FetchOpeningSingleUseCase
import no.usn.mob3000.domain.usecase.game.opening.FetchOpeningsUseCase
import no.usn.mob3000.domain.usecase.game.opening.UpdateOpeningUseCase
import no.usn.mob3000.domain.viewmodel.auth.AuthenticationViewModel

/**
 * ViewModel to track state for chess openings.
 *
 * ## Note
 *
 * Extracted from `CBViewModel` at 2024-11-14.
 *
 * @author frigvid
 * @created 2024-10-08
 */
class OpeningsViewModel(
    private val authenticationViewModel: AuthenticationViewModel = AuthenticationViewModel(),
    private val createOpeningUseCase: CreateOpeningUseCase = CreateOpeningUseCase(OpeningsRepository()),
    private val fetchOpeningsUseCase: FetchOpeningsUseCase = FetchOpeningsUseCase(OpeningsRepository()),
    private val fetchOpeningSingleUseCase: FetchOpeningSingleUseCase = FetchOpeningSingleUseCase(OpeningsRepository()),
    private val updateOpeningUseCase: UpdateOpeningUseCase = UpdateOpeningUseCase(OpeningsRepository()),
    private val deleteOpeningUseCase: DeleteOpeningUseCase = DeleteOpeningUseCase(OpeningsRepository())
) : ViewModel() {
    private val _openingState = MutableStateFlow<OpeningState>(OpeningState.Idle)
    val openingState = _openingState.asStateFlow()

    private val _openings = mutableStateOf<List<Opening>>(emptyList())
    val openings: State<List<Opening>> = _openings

    private val _openingSingle = mutableStateOf<Opening?>(null)
    val openingSingle: State<Opening?> = _openingSingle

    private val _selectedOpening = mutableStateOf<Opening?>(null)
    val selectedOpening: State<Opening?> = _selectedOpening

    private var periodicUpdateJob: Job? = null
    private val stateMutex = Mutex()

    /**
     * Function to periodically call [fetchOpenings]. This also
     * stops any existing jobs, in case there's any dirty state.
     *
     * @author frigvid
     * @created 2024-11-14
     */
    fun startPeriodicUpdates() {
        Log.d(TAG, "Starting periodic update by first canceling pending jobs.")
        stopPeriodicUpdates()

        Log.d(TAG, "Starting job!")
        periodicUpdateJob = viewModelScope.launch {
            while (isActive) {
                Log.d(TAG, "Fetching openings.")
                fetchOpenings()

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
     * @created 2024-11-14
     */
    private fun stopPeriodicUpdates() {
        periodicUpdateJob?.cancel()
        periodicUpdateJob = null
    }

    /**
     * Clean up the openings state, stop pending jobs, and
     * reset to its default state after.
     *
     * @author frigvid
     * @created 2024-11-14
     */
    suspend fun resetOpeningsState() {
        stateMutex.withLock {
            stopPeriodicUpdates()
            _openingState.emit(OpeningState.Idle)
            Log.w(TAG, "Resetting openings state! ${_openingState.value}")
        }
    }

    /**
     * Override super implementation of onCleared to additionally also
     * stop any pending jobs and related states, when clearing the view
     * model.
     *
     * @author frigvid
     * @created 2024-11-14
     */
    override fun onCleared() {
        super.onCleared()
        stopPeriodicUpdates()
    }

    /**
     * This is the main handler for fetching openings and updating the opening state.
     *
     * @author frigvid
     * @created 2024-11-14
     */
    fun fetchOpenings() {
        viewModelScope.launch {
            stateMutex.withLock {
                _openingState.emit(OpeningState.Idle)

                try {
                    fetchOpeningsUseCase().fold(
                        onSuccess = { openingsList ->
                            _openings.value = openingsList
                            _openingState.emit(OpeningState.Success)
                        },
                        onFailure = { error ->
                            Log.e(TAG, "Failure! Something went wrong while fetching openings!")
                            _openingState.emit(OpeningState.Error(Exception(error)))
                        }
                    )
                } catch (error: Exception) {
                    Log.e(TAG, "Something unknown went wrong!", error)
                    _openingState.emit(OpeningState.Error(error))
                }
            }
        }
    }

    /**
     * Fetches a single opening.
     *
     * @param openingId The ID of the opening to fetch.
     * @author frigvid
     * @created 2024-11-14
     */
    fun fetchOpeningSingle(
        openingId: String
    ) {
        viewModelScope.launch {
            try {
                fetchOpeningSingleUseCase(openingId).fold(
                    onSuccess = { opening ->
                        _openingSingle.value = opening
                    },
                    onFailure = { error ->
                        Log.e(TAG, "Something went wrong while fetching the opening with the ID: $openingId", error)
                    }
                )
            } catch (error: Exception) {
                Log.e(TAG, "Something unknown went wrong when fetching a single (1) opening!", error)
            }
        }
    }

    /**
     * Creates an opening.
     *
     * @param opening The [Opening] object to be insert.
     * @author frigvid
     * @created 2024-11-14
     */
    fun createOpening(
        opening: Opening
    ) {
        viewModelScope.launch {
            try {
                createOpeningUseCase(opening)
            } catch (error: Exception) {
                Log.e(TAG, "Something unknown went wrong while creating the opening!", error)
            }
        }
    }

    /**
     * Deletes an opening.
     *
     * @param openingId The ID of the [Opening] to be deleted.
     * @author frigvid
     * @created 2024-11-14
     */
    fun deleteOpening(
        openingId: String
    ) {
        viewModelScope.launch {
            try {
                Log.d("OpeningsViewModel", "Deleting opening with ID: $openingId")
                deleteOpeningUseCase(openingId)
            } catch (error: Exception) {
                Log.e(TAG, "Something unknown went wrong while deleting the opening!", error)
            }
        }
    }

    /**
     * Updates an opening.
     *
     * @param opening The [Opening] object to be updated.
     * @author frigvid
     * @created 2024-11-14
     */
    fun updateOpening(
        opening: Opening
    ) {
        viewModelScope.launch {
            try {
                updateOpeningUseCase(opening)
            } catch (error: Exception) {
                Log.e(TAG, "Something went wrong while updating the opening!", error)
            }
        }
    }

    fun setOpenings(openings: List<Opening>) {
        _openings.value = openings
    }

    fun setSelectedOpening(opening: Opening) {
        _selectedOpening.value = opening
    }

    /**
     * Retrieves a stored opening by its ID.
     *
     * @param id The ID of the opening to retrieve.
     * @return The Opening with the matching ID, or null if not found.
     * @author frigvid
     * @created 2024-10-09
     */
    fun getOpeningById(id: String): Opening? {
        return _openings.value.find { it.id == id }
    }

    companion object {
        /**
         * Represents 5 minutes.
         */
        private const val REFRESH_INTERVAL = 5 * 60 * 1000L
        private const val TAG = "OpeningsViewModel"
    }
}
