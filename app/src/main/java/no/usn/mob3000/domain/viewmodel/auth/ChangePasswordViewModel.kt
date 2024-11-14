package no.usn.mob3000.domain.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.domain.model.auth.error.AccountModificationError
import no.usn.mob3000.domain.model.auth.state.ChangePasswordState
import no.usn.mob3000.domain.usecase.auth.ChangePasswordUseCase

/**
 * ViewModel for account password change state.
 *
 * @author frigvid
 * @created 2024-11-13
 */
class ChangePasswordViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase = ChangePasswordUseCase()
) : ViewModel() {
    /**
     * The current [ChangePasswordState].
     *
     * @author frigvid
     * @created 2024-11-03
     */
    private val _changePasswordState = MutableStateFlow<ChangePasswordState>(ChangePasswordState.Idle)
    val changePasswordState = _changePasswordState.asStateFlow()

    /**
     * Initiates the password change process for the currently authenticated user.
     *
     * @param newPassword The password to change to.
     * @author frigvid
     * @created 2024-11-13
     */
    fun changePassword(
        newPassword: String
    ) {
        if (_changePasswordState.value !is ChangePasswordState.Idle) return

        viewModelScope.launch {
            _changePasswordState.value = ChangePasswordState.Loading

            changePasswordUseCase(newPassword).fold(
                onSuccess = {
                    _changePasswordState.value = ChangePasswordState.Success
                },
                onFailure = { error ->
                    val passwordChangeError = when (error) {
                        is Exception -> AccountModificationError.fromException(error)
                        else -> AccountModificationError.Unknown(error.message ?: "Unknown error occurred")
                    }

                    _changePasswordState.value = ChangePasswordState.Error(passwordChangeError)
                }
            )
        }
    }

    /**
     * Used to manually trigger states.
     *
     * @param state The [ChangePasswordState] to trigger.
     * @author frigvid
     * @created 2024-11-13
     */
    fun updateState(state: ChangePasswordState) {
        _changePasswordState.value = state
    }
}
