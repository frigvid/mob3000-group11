package no.usn.mob3000.domain.model.auth.state

/**
 * Sealed class representing the possible states of the account deletion process.
 *
 * @author frigvid
 * @created 2024-10-22
 */
sealed class DeleteAccountState {
    /**
     * Initial state before any account deletion attempt.
     */
    data object Idle : DeleteAccountState()

    /**
     * State while account deletion is in progress.
     */
    data object Loading : DeleteAccountState()

    /**
     * State when account deletion succeeds.
     */
    data object Success : DeleteAccountState()

    /**
     * State when account deletion fails, containing the specific error type.
     *
     * Unlike the other authentication error flows, this doesn't really
     * need its own error type. It throws a `RestException`, but given
     * this is for logging out, simply returning that should be enough.
     *
     * @param error The returned [Exception].
     */
    data class Error(val error: Exception) : DeleteAccountState()
}
