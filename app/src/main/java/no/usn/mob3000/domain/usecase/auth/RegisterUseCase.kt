package no.usn.mob3000.domain.usecase.auth

import no.usn.mob3000.data.model.auth.UserDto
import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.domain.model.User

/**
 * Registration Use Case
 *
 * Encapsules the registration logic
 *
* @property authRepository
* @author Anarox
* @created 2024-10-30
*/
class RegisterUseCase(private val authRepository: AuthRepository = AuthRepository(AuthDataSource())) {
    /**
     * Executes the login operation with the provided credentials.
     *
     * @param email The user's e-mail address.
     * @param password The user's password.
     * @return A [Result] containing either the [UserDto] data on success or an error on failure.
     * @author Anarox
     * @created 2024-11-03
     */
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<User> {
        if (!email.isValidEmail() || password.length < 8) {
            return Result.failure(IllegalArgumentException("Invalid e-mail or password is too short."))
        }
        return authRepository.register(email, password)
    }

    private fun String.isValidEmail(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()


}

