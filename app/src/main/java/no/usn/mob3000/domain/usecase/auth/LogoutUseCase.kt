package no.usn.mob3000.domain.usecase.auth

import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.domain.viewmodel.auth.AuthenticationViewModel

/**
 * Android Use Case for handling logout operations.
 *
 * @property authRepository The repository handling authentication operations.
 * @property authenticationViewModel The authentication status view model.
 * @author frigvid
 * @created 2024-10-25
 */
class LogoutUseCase(
    private val authRepository: AuthRepository = AuthRepository(AuthDataSource(), UserDataSource()),
    private val authenticationViewModel: AuthenticationViewModel = AuthenticationViewModel()
) {
    suspend operator fun invoke(): Result<Unit> = try {
        authRepository.logout()
        Result.success(Unit).onSuccess {
            authenticationViewModel.resetAuthState()
        }
    } catch (error: Exception) {
        authenticationViewModel.resetAuthState()
        Result.failure(error)
    }
}
