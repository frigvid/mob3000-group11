package no.usn.mob3000.domain.model.auth.state

/**
 * Sealed class representing the possible states of the logout process.
 *
 * @author frigvid
 * @created 2024-10-22
 */
sealed class LogoutState {
    /**
     * Initial state before any logout attempt.
     */
    data object Idle : LogoutState()

    /**
     * State while logout is in progress.
     */
    data object Loading : LogoutState()

    /**
     * State when logout succeeds.
     */
    data object Success : LogoutState()

    /**
     * State when logout fails, containing the specific error type.
     *
     * Unlike the other authentication error flows, this doesn't really
     * need its own error type. It throws a `RestException`, but given
     * this is for logging out, simply returning that should be enough.
     *
     * @param error The returned [Exception].
     */
    data class Error(val error: Exception) : LogoutState()
}
