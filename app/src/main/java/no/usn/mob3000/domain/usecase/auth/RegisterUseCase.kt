package no.usn.mob3000.domain.usecase.auth

import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.domain.model.User

/**
* @property authRepository
* @author Anarox
* @created 2024-10-30
*/


class RegisterUseCase(private val authRepository: AuthRepository = AuthRepository(AuthDataSource())) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (!email.isValidEmail() || password.length < 8) {
            return Result.failure(IllegalArgumentException("Invalid e-mail or password is too short."))
        }
        return authRepository.register(email, password)
    }

    private fun String.isValidEmail(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()


}

