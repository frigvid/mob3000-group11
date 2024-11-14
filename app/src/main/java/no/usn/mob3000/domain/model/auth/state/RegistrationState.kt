package no.usn.mob3000.domain.model.auth.state

import no.usn.mob3000.domain.model.auth.error.RegistrationError

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
