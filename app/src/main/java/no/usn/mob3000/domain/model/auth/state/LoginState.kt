package no.usn.mob3000.domain.model.auth.state

import no.usn.mob3000.domain.model.auth.error.AuthError
import no.usn.mob3000.domain.model.auth.User

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
     *
     * @param user The [User] object.
     */
    data class Success(val user: User) : LoginState()

    /**
     * State when login fails, containing the specific error type.
     *
     * @param error The returned [AuthError].
     * @see AuthError
     */
    data class Error(val error: AuthError) : LoginState()
}
