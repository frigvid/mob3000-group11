package no.usn.mob3000.domain.viewmodel.auth

import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.domain.helper.Logger

/**
 * Android Use Case for handling forgotten password operations.
 *
 * @property authRepository The repository handling authentication operations.
 * @property authenticationViewModel The authentication status view model.
 * @author frigvid
 * @created 2024-11-13
 */
class ForgottenPasswordUseCase(
    private val authRepository: AuthRepository = AuthRepository(AuthDataSource(), UserDataSource()),
    private val authenticationViewModel: AuthenticationViewModel = AuthenticationViewModel()
) {
    /**
     * Executes the forgotten password operation.
     *
     * @param email The e-mail of the user who requests a forgotten password e-mail.
     * @author frigvid
     * @created 2024-11-13
     */
    suspend operator fun invoke(
        email: String
    ): Result<Unit> {
        return authRepository.forgotPassword(email).onSuccess {
            Logger.d("Starting authentication state job.")
            authenticationViewModel.updateAuthState()
            authenticationViewModel.startPeriodicUpdates()
        }
    }
}
