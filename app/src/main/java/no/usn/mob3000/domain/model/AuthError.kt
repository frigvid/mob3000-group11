package no.usn.mob3000.domain.model

import androidx.annotation.StringRes
import no.usn.mob3000.R

/**
 * Represents authentication-related errors.
 *
 * This class hierarchy defines various authentication error states that can occur during
 * user authentication flows.
 *
 * @property messageRes The string resource ID fro the error message.
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
         * @param error The exception to analyze.
         * @return The corresponding [AuthError] instance.
         * @author frigvid
         * @created 2024-10-22
         */
        fun fromException(error: Exception): AuthError {
            return when {
                error.message?.contains("invalid_credentials") == true -> InvalidCredentials
                error.message?.contains("user_not_found") == true -> UserNotFound
                error.message?.contains("user_banned") == true -> UserBanned
                error.message?.contains("email_not_confirmed") == true -> EmailNotConfirmed
                error.message?.contains("invalid_credentials") == true -> InvalidCredentials
                else -> Unknown(error.message ?: "Unknown error occurred")
            }
        }
    }
}
