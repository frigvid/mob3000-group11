package no.usn.mob3000.domain.model.game.state

/**
 * Sealed class representing the possible states of any repertoires/groups processes.
 *
 * @author frigvid
 * @created 2024-11-15
 */
sealed class GroupState {
    /**
     * Initial state before any repertoire/group operation.
     */
    data object Idle : GroupState()

    /**
     * State while a repertoire/group operation is in progress.
     */
    data object Loading : GroupState()

    /**
     * State when a repertoire/group operation succeeds.
     */
    data object Success : GroupState()

    /**
     * State when a repertoire/group operation fails.
     *
     * @param error The returned [Exception].
     */
    data class Error(val error: Exception) : GroupState()
}
