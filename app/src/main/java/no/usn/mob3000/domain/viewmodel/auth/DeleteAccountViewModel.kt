package no.usn.mob3000.domain.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.domain.usecase.auth.DeleteAccountUseCase

/**
 * ViewModel for account deletion state.
 *
 * @property deleteAccountUseCase The Android Use Case handling account deletion business logic.
 * @author frigvid
 * @created 2024-11-03
 */
class DeleteAccountViewModel(
    private val deleteAccountUseCase: DeleteAccountUseCase = DeleteAccountUseCase()
) : ViewModel() {
    /**
     * The current [DeleteAccountState].
     *
     * @author frigvid
     * @created 2024-11-03
     */
    private val _deleteAccountState = MutableStateFlow<DeleteAccountState>(DeleteAccountState.Idle)
    val deleteAccountState = _deleteAccountState.asStateFlow()

    /**
     * Initiates the account deletion process for the currently authenticated user.
     *
     * @author frigvid
     * @created 2024-11-03
     */
    fun deleteAccount() {
        if (_deleteAccountState.value !is DeleteAccountState.Idle) return

        viewModelScope.launch {
            _deleteAccountState.value = DeleteAccountState.Loading

            deleteAccountUseCase().fold(
                onSuccess = {
                    _deleteAccountState.value = DeleteAccountState.Success
                },
                onFailure = { error ->
                    _deleteAccountState.value = DeleteAccountState.Error(Exception(error))
                }
            )
        }
    }

    fun resetState() {
        _deleteAccountState.value = DeleteAccountState.Idle
    }
}

/**
 * Sealed class representing the possible states of the account deletion process.
 *
 * @author frigvid
 * @created 2024-10-22
 */
sealed class DeleteAccountState {
    /**
     * Initial state before any account deletion attempt.
     */
    data object Idle : DeleteAccountState()

    /**
     * State while account deletion is in progress.
     */
    data object Loading : DeleteAccountState()

    /**
     * State when account deletion succeeds.
     */
    data object Success : DeleteAccountState()

    /**
     * State when account deletion fails, containing the specific error type.
     *
     * Unlike the other authentication error flows, this doesn't really
     * need its own error type. It throws a `RestException`, but given
     * this is for logging out, simply returning that should be enough.
     *
     * @param error The returned [Exception].
     */
    data class Error(val error: Exception) : DeleteAccountState()
}
