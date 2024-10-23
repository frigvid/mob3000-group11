package no.usn.mob3000.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.data.model.User
import no.usn.mob3000.domain.usecase.LoginUseCase

/**
 * ViewModel to user state and, if necessary, authentication state.
 *
 * @property loginUseCase The Android Use Case handling login business logic.
 * @author frigvid
 * @created 2024-10-21
 */
class LoginViewModel(
    private val loginUseCase: LoginUseCase = LoginUseCase()
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    /**
     * Initiates the login process with the provided credentials.
     *
     * @param email The user's e-mail address.
     * @param password The user's password.
     * @author frigvid
     * @created 2024-10-22
     */
    fun login(
        email: String,
        password: String
    ) {
        if (_loginState.value !is LoginState.Idle) return

        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            loginUseCase(email, password).fold(
                onSuccess = { user ->
                    _loginState.value = LoginState.Success(user)
                },
                onFailure = { error ->
                    _loginState.value = LoginState.Error(error.message ?: "Unknown error occurred.")
                }
            )
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

/**
 * Sealed class representing the possible states of the login process.
 *
 * @author frigvid
 * @created 2024-10-22
 */
sealed class LoginState {
    /**
     * Initial state before any login attempt.
     */
    data object Idle : LoginState()

    /**
     * State while login is in progress.
     */
    data object Loading : LoginState()

    /**
     * State when login succeeds, containing the user data.
     */
    data class Success(val user: User) : LoginState()

    /**
     * State when login fails, containing the error message.
     *
     * TODO: Expand errors to be more specific by type.
     */
    data class Error(val message: String) : LoginState()
}
