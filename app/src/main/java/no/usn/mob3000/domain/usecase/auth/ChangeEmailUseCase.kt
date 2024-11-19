package no.usn.mob3000.domain.usecase.auth

import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.domain.helper.Logger
import no.usn.mob3000.domain.viewmodel.auth.AuthenticationViewModel

/**
 * Android Use Case for handling e-mail change operations.
 *
 * @property authRepository The repository handling authentication operations.
 * @property authenticationViewModel The authentication status view model.
 * @author frigvid
 * @created 2024-11-13
 */
class ChangeEmailUseCase(
    private val authRepository: AuthRepository = AuthRepository(AuthDataSource(), UserDataSource()),
    private val authenticationViewModel: AuthenticationViewModel = AuthenticationViewModel()
) {
    /**
     * Executes the e-mail change operation.
     *
     * @param newEmail The e-mail address to change to.
     * @author frigvid
     * @created 2024-11-13
     */
    suspend operator fun invoke(
        newEmail: String
    ): Result<Unit> {
        if (!newEmail.isValidEmail()) {
            return Result.failure(IllegalArgumentException("Invalid e-mail format"))
        }

        return authRepository.changeEmail(newEmail).onSuccess {
            Logger.d("Starting authentication state job.")
            authenticationViewModel.updateAuthState()
            authenticationViewModel.startPeriodicUpdates()
        }
    }

    private fun String.isValidEmail(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
