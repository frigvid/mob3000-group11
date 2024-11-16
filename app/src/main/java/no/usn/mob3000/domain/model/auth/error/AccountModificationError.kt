package no.usn.mob3000.domain.model.auth.error

import androidx.annotation.StringRes
import io.github.jan.supabase.gotrue.exception.AuthRestException
import io.github.jan.supabase.gotrue.exception.AuthWeakPasswordException
import no.usn.mob3000.R

/**
 * Represents account modification related errors. E.g. when updating the user's password or
 * e-mail address.
 *
 * @property messageRes The string resource ID for the error message.
 * @author frigvid
 * @created 2024-11-12
 */
sealed class AccountModificationError(
    @StringRes val messageRes: Int
) {
    /**
     * Error indicating the passwords do not match.
     *
     * This is a manual trigger.
     *
     * ## Example
     *
     * ```
     * ChangePasswordState.Error(AccountModificationError.PasswordsMustMatch("Passwords do not match!"))
     * ```
     */
    data object PasswordMustMatch : AccountModificationError(R.string.auth_error_passwords_must_match)

    /**
     * Error indicating the e-mail addresses do not match.
     *
     * This is a manual trigger.
     *
     * ## Usage
     *
     * ```
     * ChangeEmailState.Error(AccountModificationError.PasswordsMustMatch("E-mail addresses do not match!"))
     * ```
     */
    data object EmailMustMatch : AccountModificationError(R.string.auth_error_emails_must_match)

    /**
     * Error indicating invalid email format.
     */
    data object InvalidEmailFormat : AccountModificationError(R.string.auth_error_invalid_email_format)

    /**
     * Error for unknown error.
     *
     * @param message The error message to display.
     */
    data class Unknown(val message: String) :
        AccountModificationError(R.string.auth_error_unexpected_failure)

    companion object {
        /**
         * Creates an appropriate [AccountModificationError] instance from an [Exception].
         *
         * Analyzes the exception's error message to determine the most specific error type.
         * Falls back to [Unknown] if no specific match is found.
         *
         * See also [Supabase's error-codes debugging docs](https://supabase.com/docs/guides/auth/debugging/error-codes).
         *
         * @param error The generic [Exception] object to analyze.
         * @return The corresponding [AccountModificationError] instance.
         * @author frigvid
         * @created 2024-11-13
         */
        fun fromException(error: Exception): AccountModificationError {
            return when (error) {
                is AuthRestException -> {
                    /* TODO: See `RegistrationError`'s `fromException` for additional details. */
                    when {
                        error.message?.contains("invalid email", ignoreCase = true) == true ->
                            InvalidEmailFormat

                        else -> Unknown(error.message ?: "Unknown error occurred")
                    }
                }

                else -> Unknown(error.message ?: "Unknown error occurred")
            }
        }
    }
}
