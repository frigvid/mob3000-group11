package no.usn.mob3000.domain.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.domain.model.auth.RegistrationError
import no.usn.mob3000.domain.usecase.auth.RegisterUseCase

/**
 * ViewModel for user registration state.
 *
 * @property registerUseCase The Android Use Case handling registration business logic.
 * @author Anarox1111, frigvid
 * @created 2024-10-21
 */
class RegistrationViewModel(
    private val registerUseCase: RegisterUseCase = RegisterUseCase()
): ViewModel() {
    /**
     * The current [RegistrationState].
     *
     * @author frigvid
     * @created 2024-11-03
     */
    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState = _registrationState.asStateFlow()

    /**
     * Initiates the registration process with the provided credentials.
     *
     * @param email The user's e-mail address.
     * @param password The user's password.
     * @author Anarox, frigvid
     * @created 2024-10-30
     */
    fun register(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading
            registerUseCase(email, password).fold(
                onSuccess = {
                    _registrationState.value = RegistrationState.Success
                },
                onFailure = { error ->
                    val registrationError = when (error) {
                        is Exception -> RegistrationError.fromException(error)
                        else -> RegistrationError.Unknown(error.message ?: "Unknown error occurred")
                    }

                    _registrationState.value = RegistrationState.Error(registrationError)
                }
            )
        }
    }

    /**
     * Used to manually trigger states.
     *
     * @param state The [RegistrationState] to trigger.
     * @author frigvid
     * @created 2024-11-03
     */
    fun updateState(state: RegistrationState) {
        _registrationState.value = state
    }

    fun resetState() {
        _registrationState.value = RegistrationState.Idle
    }
}


/**
 * Sealed class representing the possible states of the registration process.
 *
 * @author frigvid
 * @created 2024-11-03
 */
sealed class RegistrationState {
    /**
     * Initial state before any attempt.
     */
    data object Idle : RegistrationState()

    /**
     * State while in progress.
     */
    data object Loading : RegistrationState()

    /**
     * State when success.
     */
    data object Success : RegistrationState()

    /**
     * State when login fails, containing the specific error type.
     *
     * @param error The returned [RegistrationError].
     * @see RegistrationError
     */
    data class Error(val error: RegistrationError) : RegistrationState()
}
