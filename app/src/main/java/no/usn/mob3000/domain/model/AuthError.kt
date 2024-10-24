package no.usn.mob3000.domain.model

import android.util.Log
import androidx.annotation.StringRes
import io.github.jan.supabase.gotrue.exception.AuthRestException
import no.usn.mob3000.R

/**
 * Represents authentication-related errors.
 *
 * This class hierarchy defines various authentication error states that can occur during
 * user authentication flows.
 *
 * @property messageRes The string resource ID from the error message.
 * @author frigvid
 * @created 2024-10-22
 */
sealed class AuthError(
    @StringRes val messageRes: Int
) {
    /**
     * Error indicating invalid login credentials were provided.
     */
    data object InvalidCredentials : AuthError(R.string.auth_error_invalid_credentials)

    /**
     * Error indicating the requested user account does not exist.
     */
    data object UserNotFound : AuthError(R.string.auth_error_user_not_found)

    /**
     * Error indicating the user account is currently banned or suspended.
     */
    data object UserBanned : AuthError(R.string.auth_error_user_banned)

    /**
     * Error indicating email verification is required before login.
     */
    data object EmailNotConfirmed : AuthError(R.string.auth_error_email_not_confirmed)

    /**
     * Error for unexpected failures with custom error messages.
     *
     * TODO: Fix this up a bit so it actually returns the localized unknown error message
     *       together with the error's message.
     *
     * @property message Detailed error message describing the failure
     */
    data class Unknown(val message: String) : AuthError(R.string.auth_error_unexpected_failure)

    companion object {
        /**
         * Creates an appropriate [AuthError] instance from an [Exception].
         *
         * Analyzes the exception's error message to determine the most specific error type.
         * Falls back to [Unknown] if no specific match is found.
         *
         * See also [Supabase's error-codes debugging docs](https://supabase.com/docs/guides/auth/debugging/error-codes).
         *
         * Note that you **MUST** use [AuthRestException] and specifically target the error's parameter
         * `error.error`. *This* is where the error code is stored, it is *not* stored in
         * `error.code` The `error.code` is `null` while `error.error` contains the actual error
         * code for ... some inexplicable reason.
         *
         * TODO: Investigate why the [AuthRestException] only seems to return the `AuthErrorCode`
         *       for `invalid_credentials`, even when it shouldn't.
         *
         * @param error The [AuthRestException] exception to analyze.
         * @return The corresponding [AuthError] instance.
         * @author frigvid
         * @created 2024-10-22
         */
        fun fromException(error: AuthRestException): AuthError {
            /* See above ↑↑↑ TODO: Log.d("AuthError", "Error error: ${error.error}. Error code: ${error.errorCode}. Error status code: ${error.statusCode}") */
            return when {
                error.error.contains("user_not_found") -> UserNotFound
                error.error.contains("user_banned") -> UserBanned
                error.error.contains("email_not_confirmed") -> EmailNotConfirmed
                error.error.contains("invalid_credentials") -> InvalidCredentials
                else -> Unknown(error.message ?: "Unknown error occurred")
            }
        }
    }
}
