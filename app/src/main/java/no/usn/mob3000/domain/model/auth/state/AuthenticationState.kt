package no.usn.mob3000.domain.model.auth.state

/**
 * The authentication state objects.
 *
 * ## Usage
 *
 * See `AuthenticationViewModel` for usage example.
 *
 * @author frigvid
 * @created 2024-11-07
 */
sealed class AuthenticationState {
    /**
     * Object stored while in-progress.
     */
    data object Loading : AuthenticationState()

    /**
     * The authentication state of users.
     *
     * @property userId The UUID of the authenticated user.
     * @property isAdmin Whether the authenticated user is an administrator.
     * @property lastChecked The timestamp when this information was last checked.
     * @author frigvid
     * @created 2024-11-07
     */
    data class Authenticated(
        val userId: String,
        val isAdmin: Boolean = false,
        val lastChecked: Long = 0
    ) : AuthenticationState()

    /**
     * Object stored if there is an error.
     *
     * @property error The [Exception].
     */
    data class Error(
        val error: Exception
    ) : AuthenticationState()

    /**
     * Object stored if there is no authenticated user.
     */
    data object Unauthenticated : AuthenticationState()
}
