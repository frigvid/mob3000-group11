package no.usn.mob3000.domain.usecase.auth

import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource

/**
 * Android Use Case for handling registration operations.
 *
 * This encapsulates the business logic for the registration process, and serves as
 * a bridge between the UI and Data layers.
 *
 * @property authRepository The repository handling authentication operations.
 * @author Anarox1111
 * @contributor frigvid
 * @created 2024-10-30
 */
class RegisterUseCase(
    private val authRepository: AuthRepository = AuthRepository(AuthDataSource(), UserDataSource())
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<Unit> {
        if (!email.isValidEmail() || password.length < 8) {
            return Result.failure(
                IllegalArgumentException("Invalid e-mail or password is too short.")
            )
        }

        return authRepository.register(email, password)
    }

    private fun String.isValidEmail(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
