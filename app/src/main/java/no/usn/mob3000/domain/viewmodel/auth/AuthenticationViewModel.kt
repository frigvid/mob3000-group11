package no.usn.mob3000.domain.viewmodel.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import no.usn.mob3000.domain.model.auth.state.AuthenticationState
import no.usn.mob3000.domain.usecase.auth.status.IsAdministratorUseCase
import no.usn.mob3000.domain.usecase.auth.status.IsAuthenticatedUseCase

/**
 * ViewModel to track state for whether a user is authenticated and if they're an administrator.
 *
 * ## Example usage
 *
 * This is pretty easy to use, all you need to do is collect the authentication state as, well,
 * state. And then you need a `LaunchedEffect` to actually fetch and update this data. See this
 * [StackOverflow](https://stackoverflow.com/a/78191331) answer for why.
 *
 * Below is an example showing how to use this assuming you pass the entire view model, but
 * remember to only pass the authentication state's value to the screen instead of this. It's
 * practically the same in terms of implementation.
 *
 * ```
 * // Assume the view model is injected as authenticationViewModel.
 * val authState by remember { authenticationViewModel.authState }.collectAsState()
 * LaunchedEffect(Unit) {
 *     authenticationViewModel.updateAuthState()
 * }
 *
 * // Stateful implementation from SettingsScreen per 2024-11-11.
 * val authStatusText = when (val state = authState) {
 *     is AuthenticationState.Authenticated -> {
 *         val lastCheckedTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
 *             .format(Date(state.lastChecked))
 *         "UserId: ${state.userId} | Admin: ${state.isAdmin} | Last check: $lastCheckedTime"
 *     }
 *     is AuthenticationState.Loading -> "Checking authentication..."
 *     is AuthenticationState.Unauthenticated -> "Not authenticated"
 *     is AuthenticationState.Error -> "Auth Error: ${state.error.message}"
 * }
 * ```
 *
 * ## Notes
 *
 * See [AuthenticationState] for the companion objects used to show the user's sate.
 *
 * Additionally, see `SettingsScreen` for an example of how this is used in practice.
 *
 * @author frigvid
 * @created 2024-11-07
 */
class AuthenticationViewModel(
    private val isAdministratorUseCase: IsAdministratorUseCase = IsAdministratorUseCase(),
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase = IsAuthenticatedUseCase()
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthenticationState>(AuthenticationState.Unauthenticated)
    val authState: StateFlow<AuthenticationState> = _authState.asStateFlow()

    private var periodicUpdateJob: Job? = null

    /**
     * Ensures explicit coroutine lock to avoid any race conditions.
     * It's largely unnecessary given the implementation. But due to
     * this view model being used to track authentication state, I'd
     * rather be doubly sure.
     *
     * @author frigvid
     * @created 2024-11-11
     */
    private val stateMutex = Mutex()

    /**
     * Function to periodically call [updateAuthState]. This also
     * stops any existing jobs, in case there's any dirty state.
     */
    fun startPeriodicUpdates() {
        Log.d(TAG, "Starting periodic update by canceling pending jobs.")
        stopPeriodicUpdates()
        periodicUpdateJob = viewModelScope.launch {
            while (isActive) {
                delay(REFRESH_INTERVAL)
                Log.d(TAG, "Updating authentication state.")
                updateAuthState()
            }
        }
    }

    /**
     * Function to clean up job state by stopping any pending
     * jobs and removing any stored reference to it.
     */
    private fun stopPeriodicUpdates() {
        periodicUpdateJob?.cancel()
        periodicUpdateJob = null
    }

    /**
     * This function serves as the main handler for updating the authentication state.
     *
     * ## Usage
     *
     * When needing to immediately perform an update action, this is the function to
     * call. See `LoginUseCase` for an implementation example. Additionally, you should
     * call [startPeriodicUpdates] after this one.
     *
     * ```
     * // Assumes this view model is instantiated, or these functions are injected into
     * // a composable function.
     * authenticationViewModel.updateAuthState()
     * authenticationViewModel.startPeriodicUpdates()
     * ```
     */
    fun updateAuthState() {
        Log.i(TAG, "Updating authentication state!")
        viewModelScope.launch {
            stateMutex.withLock {
                _authState.emit(AuthenticationState.Loading)

                try {
                    isAuthenticatedUseCase().fold(
                        onSuccess = { userInfo ->
                            Log.d(TAG, "Success! Checking if user data is null...")
                            if (userInfo == null) {
                                _authState.emit(AuthenticationState.Unauthenticated)
                                return@fold
                            }

                            Log.d(TAG, "User data is not null. User ID: ${userInfo.id}. Checking administrator access...")
                            isAdministratorUseCase().fold(
                                onSuccess = { isAdmin ->
                                    val authObject = AuthenticationState.Authenticated(
                                        userId = userInfo.id,
                                        isAdmin = isAdmin,
                                        lastChecked = System.currentTimeMillis()
                                    )

                                    Log.d(TAG, "Auth state update: $authObject")
                                    _authState.emit(authObject)
                                },
                                onFailure = { error ->
                                    Log.e(TAG, "Failure! Something went wrong while checking administrator access!", error)
                                    _authState.emit(AuthenticationState.Error(Exception(error)))
                                }
                            )
                        },
                        onFailure = { error ->
                            /* TODO: No session token found is expected behavior. Checking of what
                            *        type of error occurs, so that if it's a regular session missing
                            *        error, then it logs as a debug message and not as an error log.
                            *        <br/>
                            *        Though, if it's an unknown error, then it should be logged as
                            *        such.
                            */
                            Log.e(TAG, "Failure! Something went wrong while checking for user data!", error)
                            _authState.emit(AuthenticationState.Error(Exception(error)))
                        }
                    )
                } catch (error: Exception) {
                    Log.e(TAG, "Something unknown went wrong!", error)
                    _authState.emit(AuthenticationState.Error(Exception(error)))
                }
            }
        }
    }

    /**
     * Clean up the authentication state, stop pending jobs, and
     * reset to its default state after.
     */
    suspend fun resetAuthState() {
        stateMutex.withLock {
            stopPeriodicUpdates()
            _authState.emit(AuthenticationState.Unauthenticated)
            Log.w(TAG, "Resetting authentication state! ${_authState.value}")
        }
    }

    /**
     * Override super implementation of onCleared to additionally also
     * stop any pending jobs and related states, when clearing the view
     * model.
     */
    override fun onCleared() {
        super.onCleared()
        stopPeriodicUpdates()
    }

    companion object {
        /**
         * Represents 5 minutes.
         */
        private const val REFRESH_INTERVAL = 5 * 60 * 1000L

        /**
         * TODO: Remove when chess functionality is merged with Logger wrapper function.
         */
        private const val TAG = "AuthenticationViewModel"
    }
}
