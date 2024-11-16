package no.usn.mob3000.domain.model.auth.state

import no.usn.mob3000.domain.model.auth.error.AccountModificationError

/**
 * Sealed class representing the possible states of the account e-mail change process.
 *
 * @author frigvid
 * @created 2024-11-13
 */
sealed class ChangeEmailState {
    /**
     * Initial state before any e-mail change attempt.
     */
    data object Idle : ChangeEmailState()

    /**
     * State while e-mail change is in progress.
     */
    data object Loading : ChangeEmailState()

    /**
     * State when e-mail change succeeds.
     */
    data object Success : ChangeEmailState()

    /**
     * State when e-mail change fails, containing the specific error type.
     *
     * ## Notes
     *
     * See [DeleteAccountState]'s Error data class docstring for more information.
     *
     * @param error The returned [AccountModificationError].
     */
    data class Error(val error: AccountModificationError) : ChangeEmailState()
}
