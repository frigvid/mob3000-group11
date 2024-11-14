package no.usn.mob3000.domain.viewmodel.auth

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.gotrue.exception.AuthRestException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.usn.mob3000.domain.model.auth.error.AuthError
import no.usn.mob3000.domain.model.auth.User
import no.usn.mob3000.domain.model.auth.state.LoginState
import no.usn.mob3000.domain.usecase.auth.LoginUseCase

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
    /**
     * The current [LoginState].
     *
     * @author frigvid
     * @created 2024-10-21
     */
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
                    Log.d("LoginViewModel", user.toString())
                    _loginState.value = LoginState.Success(user)
                },
                onFailure = { error ->
                    /* TODO: Investigate why this became necessary.
                     *       See: java.lang.Exception cannot be cast to io.github.jan.supabase.gotrue.exception.AuthRestException
                     */
                    val authError = when (error) {
                        is AuthRestException -> AuthError.fromException(error)
                        else -> AuthError.Unknown(error.message ?: "Unknown error occurred")
                    }

                    _loginState.value = LoginState.Error(authError)
                }
            )
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}
