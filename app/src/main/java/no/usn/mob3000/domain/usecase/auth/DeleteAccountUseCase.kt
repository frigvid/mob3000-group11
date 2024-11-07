package no.usn.mob3000.domain.usecase.auth;

import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource

/**
 * Android Use Case for handling account deletion operations.
 *
 * @property authRepository The repository handling authentication operations.
 * @author frigvid
 * @created 2024-10-25
 */
class DeleteAccountUseCase(
    private val authRepository: AuthRepository = AuthRepository(AuthDataSource(), UserDataSource())
) {
    suspend operator fun invoke(): Result<Unit> = try {
        authRepository.delete()
        Result.success(Unit)
    } catch (error: Exception) {
        Result.failure(error)
    }
}
