package no.usn.mob3000.domain.model.game.state

/**
 * Sealed class representing the possible states of any opening processes.
 *
 * @author frigvid
 * @created 2024-11-14
 */
sealed class OpeningState {
    /**
     * Initial state before any opening operation.
     */
    data object Idle : OpeningState()

    /**
     * State while an opening operation is in progress.
     */
    data object Loading : OpeningState()

    /**
     * State when an opening operation succeeds.
     */
    data object Success : OpeningState()

    /**
     * State when an opening operation fails.
     *
     * @param error The returned [Exception].
     */
    data class Error(val error: Exception) : OpeningState()
}
