package no.usn.mob3000.domain.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.domain.usecase.auth.LogoutUseCase

/**
 * ViewModel for account logout state.
 *
 * @property logoutUseCase The Android Use Case handling logout business logic.
 * @author frigvid
 * @created 2024-11-03
 */
class LogoutViewModel(
    private val logoutUseCase: LogoutUseCase = LogoutUseCase()
) : ViewModel() {
    /**
     * The current [LogoutState].
     *
     * @author frigvid
     * @created 2024-11-03
     */
    private val _logoutState = MutableStateFlow<LogoutState>(LogoutState.Idle)
    val logoutState = _logoutState.asStateFlow()

    /**
     * Initiates the logout process for the currently authenticated user.
     *
     * @author frigvid
     * @created 2024-11-03
     */
    fun logout() {
        if (_logoutState.value !is LogoutState.Idle) return

        viewModelScope.launch {
            _logoutState.value = LogoutState.Loading

            logoutUseCase().fold(
                onSuccess = {
                    _logoutState.value = LogoutState.Success
                },
                onFailure = { error ->
                    _logoutState.value = LogoutState.Error(Exception(error))
                }
            )
        }
    }

    fun resetState() {
        _logoutState.value = LogoutState.Idle
    }
}

/**
 * Sealed class representing the possible states of the logout process.
 *
 * @author frigvid
 * @created 2024-10-22
 */
sealed class LogoutState {
    /**
     * Initial state before any logout attempt.
     */
    data object Idle : LogoutState()

    /**
     * State while logout is in progress.
     */
    data object Loading : LogoutState()

    /**
     * State when logout succeeds.
     */
    data object Success : LogoutState()

    /**
     * State when logout fails, containing the specific error type.
     *
     * Unlike the other authentication error flows, this doesn't really
     * need its own error type. It throws a `RestException`, but given
     * this is for logging out, simply returning that should be enough.
     *
     * @param error The returned [Exception].
     */
    data class Error(val error: Exception) : LogoutState()
}
