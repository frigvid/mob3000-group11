package no.usn.mob3000.domain.usecase

import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.model.auth.UserDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.domain.model.User

/**
 * Android Use Case for handling login operations.
 *
 * This encapsulates the business logic for the login process, and serves as
 * a bridge between the UI and Data layers.
 *
 * @property authRepository The repository handling authentication operations.
 * @author frigvid
 * @created 2024-10-22
 */
class LoginUseCase(
    private val authRepository: AuthRepository = AuthRepository(AuthDataSource())
) {
    /**
     * Executes the login operation with the provided credentials.
     *
     * @param email The user's e-mail address.
     * @param password The user's password.
     * @return A [Result] containing either the [UserDto] data on success or an error on failure.
     * @author frigvid
     * @created 2024-10-22
     */
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<User> {
        if (!email.isValidEmail()) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }

        if (password.length < 8) {
            return Result.failure(IllegalArgumentException("Password must be at least 8 characters"))
        }

        return authRepository.login(email, password)
    }


    private fun String.isValidEmail(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
