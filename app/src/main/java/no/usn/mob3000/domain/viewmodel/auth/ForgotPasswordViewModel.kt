package no.usn.mob3000.domain.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.gotrue.exception.AuthRestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.domain.model.auth.error.AuthError
import no.usn.mob3000.domain.model.auth.state.ForgotPasswordState

/**
 * ViewModel for forgotten password request state.
 *
 * @author frigvid
 * @created 2024-11-13
 */
class ForgotPasswordViewModel(
    private val forgottenPasswordUseCase: ForgottenPasswordUseCase = ForgottenPasswordUseCase()
) : ViewModel() {
    /**
     * The current [ForgotPasswordState].
     *
     * @author frigvid
     * @created 2024-11-03
     */
    private val _forgotPasswordState = MutableStateFlow<ForgotPasswordState>(ForgotPasswordState.Idle)
    val forgotPasswordState = _forgotPasswordState.asStateFlow()

    /**
     * Initiates the forgotten password request process for the currently authenticated user.
     *
     * @param email The password to change to.
     * @author frigvid
     * @created 2024-11-13
     */
    fun forgotPassword(
        email: String
    ) {
        if (_forgotPasswordState.value !is ForgotPasswordState.Idle) return

        viewModelScope.launch {
            _forgotPasswordState.value = ForgotPasswordState.Loading

            forgottenPasswordUseCase(email).fold(
                onSuccess = {
                    _forgotPasswordState.value = ForgotPasswordState.Success
                },
                onFailure = { error ->
                    /** See [LoginViewModel]'s equivalent variable for more information. */
                    val forgotPasswordError = when (error) {
                        is AuthRestException -> AuthError.fromException(error)
                        else -> AuthError.Unknown(error.message ?: "Unknown error occurred")
                    }

                    _forgotPasswordState.value = ForgotPasswordState.Error(forgotPasswordError)
                }
            )
        }
    }

    /**
     * Used to manually trigger states.
     *
     * @param state The [ForgotPasswordState] to trigger.
     * @author frigvid
     * @created 2024-11-13
     */
    fun updateState(state: ForgotPasswordState) {
        _forgotPasswordState.value = state
    }
}
