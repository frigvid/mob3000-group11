package no.usn.mob3000.domain.model.auth.error

import androidx.annotation.StringRes
import io.github.jan.supabase.gotrue.exception.AuthRestException
import io.github.jan.supabase.gotrue.exception.AuthWeakPasswordException
import no.usn.mob3000.R

/**
 * Represents authentication-related errors.
 *
 * This class defines various registration error states that can occur during
 * user registration.
 *
 * @property messageRes The string resource ID for the error message.
 * @author Anarox1111, frigvid
 * @created 2024-11-03
 */
sealed class RegistrationError(
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
     * RegistrationState.Error(RegistrationError.PasswordsMustMatch("Passwords do not match!"))
     * ```
     */
    data object PasswordsMustMatch : RegistrationError(R.string.auth_error_passwords_must_match)

    /**
     * Error indicating email is already registered.
     */
    data object EmailAlreadyExists : RegistrationError(R.string.auth_error_email_exists)

    /**
     * Error indicating that password is too weak.
     *
     * @param reasons The reason the password was rejected.
     */
    data class WeakPassword(val reasons: List<String>) :
        RegistrationError(R.string.auth_error_weak_password)

    /**
     * Error indicating invalid email format.
     */
    data object InvalidEmailFormat : RegistrationError(R.string.auth_error_invalid_email_format)

    /**
     * Error for unknown error.
     *
     * @param message The error message to display.
     */
    data class Unknown(val message: String) :
        RegistrationError(R.string.auth_error_unexpected_failure)

    companion object {
        /**
         * Creates an appropriate [RegistrationError] instance from an [Exception].
         *
         * Analyzes the exception's error message to determine the most specific error type.
         * Falls back to [Unknown] if no specific match is found.
         *
         * See also [Supabase's error-codes debugging docs](https://supabase.com/docs/guides/auth/debugging/error-codes).
         *
         * @param error The generic [Exception] object to analyze.
         * @return The corresponding [RegistrationError] instance.
         * @author frigvid
         * @created 2024-11-03
         */
        fun fromException(error: Exception): RegistrationError {
            return when (error) {
                is AuthWeakPasswordException -> WeakPassword(error.reasons)

                is AuthRestException -> {
                    /* TODO: Investigate how well this works. Supabase-kt error message returns are
                     *       somewhat dubious at the best of times.
                     */
                    when {
                        error.message?.contains("already registered", ignoreCase = true) == true ->
                            EmailAlreadyExists

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
