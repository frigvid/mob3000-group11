package no.usn.mob3000.domain.model

import androidx.annotation.StringRes
import io.github.jan.supabase.gotrue.exception.AuthRestException
import no.usn.mob3000.R
import no.usn.mob3000.domain.model.AuthError.EmailNotConfirmed
import no.usn.mob3000.domain.model.AuthError.InvalidCredentials
import no.usn.mob3000.domain.model.AuthError.Unknown
import no.usn.mob3000.domain.model.AuthError.UserBanned
import no.usn.mob3000.domain.model.AuthError.UserNotFound


/**
 * Represents authentication-related errors.
 *
 * This class defines various registration error states that can occur during
 * user registration.
 *
 * @author Anarox
 * @created 2024-11-03
 */
sealed class RegistrationError(
    @StringRes val messageRes: Int
) {
    /**
     * Error indicating email is already registered.
     */
    data object EmailAlreadyExists : RegistrationError(R.string.error_email_already_exists)

    /**
     * Error indicating that password is too weak.
     */
    data object WeakPassword : RegistrationError(R.string.error_weak_password)

    /**
     * Error indicating invalid email format.
     */
    data object  InvalidEmailFormat : RegistrationError(R.string.error_invalid_email_format)

    /**
     * Error for unknown error.
     */
    data class UnknownError(val message: String): RegistrationError(R.string.error_unknown)

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
