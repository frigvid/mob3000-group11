package no.usn.mob3000.domain.model.auth.state

import no.usn.mob3000.domain.model.auth.error.AccountModificationError

/**
 * Sealed class representing the possible states of the account password change process.
 *
 * @author frigvid
 * @created 2024-11-13
 */
sealed class ChangePasswordState {
    /**
     * Initial state before any password change attempt.
     */
    data object Idle : ChangePasswordState()

    /**
     * State while password change is in progress.
     */
    data object Loading : ChangePasswordState()

    /**
     * State when password change succeeds.
     */
    data object Success : ChangePasswordState()

    /**
     * State when password change fails, containing the specific error type.
     *
     * ## Notes
     *
     * See [DeleteAccountState]'s Error data class docstring for more information.
     *
     * @param error The returned [AccountModificationError].
     */
    data class Error(val error: AccountModificationError) : ChangePasswordState()
}
