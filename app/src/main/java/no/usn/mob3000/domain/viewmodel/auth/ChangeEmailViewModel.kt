package no.usn.mob3000.domain.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.domain.model.auth.error.AccountModificationError
import no.usn.mob3000.domain.model.auth.state.ChangeEmailState
import no.usn.mob3000.domain.usecase.auth.ChangeEmailUseCase

/**
 * ViewModel for account e-mail address change state.
 *
 * @author frigvid
 * @created 2024-11-13
 */
class ChangeEmailViewModel(
    private val changeEmailUseCase: ChangeEmailUseCase = ChangeEmailUseCase()
) : ViewModel() {
    /**
     * The current [ChangeEmailState].
     *
     * @author frigvid
     * @created 2024-11-03
     */
    private val _changeEmailState = MutableStateFlow<ChangeEmailState>(ChangeEmailState.Idle)
    val changeEmailState = _changeEmailState.asStateFlow()

    /**
     * Initiates the e-mail address change process for the currently authenticated user.
     *
     * @param newEmail The e-mail address to change to.
     * @author frigvid
     * @created 2024-11-13
     */
    fun changeEmail(
        newEmail: String
    ) {
        if (_changeEmailState.value !is ChangeEmailState.Idle) return

        viewModelScope.launch {
            _changeEmailState.value = ChangeEmailState.Loading

            changeEmailUseCase(newEmail).fold(
                onSuccess = {
                    _changeEmailState.value = ChangeEmailState.Success
                },
                onFailure = { error ->
                    val emailChangeError = when (error) {
                        is Exception -> AccountModificationError.fromException(error)
                        else -> AccountModificationError.Unknown(error.message ?: "Unknown error occurred")
                    }

                    _changeEmailState.value = ChangeEmailState.Error(emailChangeError)
                }
            )
        }
    }

    /**
     * Used to manually trigger states.
     *
     * @param state The [ChangeEmailState] to trigger.
     * @author frigvid
     * @created 2024-11-13
     */
    fun updateState(state: ChangeEmailState) {
        _changeEmailState.value = state
    }
}
