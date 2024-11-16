package no.usn.mob3000.domain.model.auth.state

import no.usn.mob3000.domain.model.auth.error.AuthError

/**
 * Sealed class representing the possible states of the forgotten password request process.
 *
 * @author frigvid
 * @created 2024-11-13
 */
sealed class ForgotPasswordState {
    /**
     * Initial state before any forgotten password request attempt.
     */
    data object Idle : ForgotPasswordState()

    /**
     * State while forgotten password request is in progress.
     */
    data object Loading : ForgotPasswordState()

    /**
     * State when forgotten password request succeeds.
     */
    data object Success : ForgotPasswordState()

    /**
     * State when forgotten password request fails, containing the specific error type.
     *
     * ## Notes
     *
     * See [DeleteAccountState]'s Error data class docstring for more information.
     *
     * @param error The returned [AuthError].
     */
    data class Error(val error: AuthError) : ForgotPasswordState()
}
