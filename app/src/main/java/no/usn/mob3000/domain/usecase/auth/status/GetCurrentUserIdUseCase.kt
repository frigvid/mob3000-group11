package no.usn.mob3000.domain.usecase.auth

import no.usn.mob3000.domain.repository.IAuthRepository

/**
 *
 * This class is responsible for getting the current user's ID.
 *
 * @author 258030
 * @created 2024-11-09
 */
class GetCurrentUserIdUseCase(
    private val authRepository: IAuthRepository
) {
    suspend fun getCurrentUserId(): String {
        return authRepository.getCurrentUserId()
    }
}
