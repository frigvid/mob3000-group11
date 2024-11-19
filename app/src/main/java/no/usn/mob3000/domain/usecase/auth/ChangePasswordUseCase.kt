package no.usn.mob3000.domain.usecase.auth

import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.viewmodel.auth.AuthenticationViewModel

/**
 * Android Use Case for handling password change operations.
 *
 * @property authRepository The repository handling authentication operations.
 * @property authenticationViewModel The authentication status view model.
 * @author frigvid
 * @created 2024-11-13
 */
class ChangePasswordUseCase(
    private val authRepository: AuthRepository = AuthRepository(AuthDataSource(), UserDataSource()),
    private val authenticationViewModel: AuthenticationViewModel = AuthenticationViewModel()
) {
    /**
     * Executes the password change operation.
     *
     * @param newPassword The password to change to.
     * @author frigvid
     * @created 2024-11-13
     */
    suspend operator fun invoke(
        newPassword: String
    ): Result<Unit> {
        return authRepository.changePassword(newPassword).onSuccess {
            Logger.d("Starting authentication state job.")
            authenticationViewModel.updateAuthState()
            authenticationViewModel.startPeriodicUpdates()
        }
    }
}
