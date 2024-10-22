package no.usn.mob3000.domain.usecase

import no.usn.mob3000.data.repository.AuthRepository
import no.usn.mob3000.data.model.User
import no.usn.mob3000.data.source.remote.AuthDataSource

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
     * @return A [Result] containing either the [User] data on success or an error on failure.
     * @author frigvid
     * @created 2024-10-22
     */
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<User> {
        return authRepository.login(email, password)
    }
}
