package no.usn.mob3000.domain.usecase.auth

import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource

/**
 * Android Use Case for handling logout operations.
 *
 * @property authRepository The repository handling authentication operations.
 * @author frigvid
 * @created 2024-10-25
 */
class LogoutUseCase(
    private val authRepository: AuthRepository = AuthRepository(AuthDataSource())
) {
    suspend operator fun invoke(): Unit = authRepository.logout()
}
