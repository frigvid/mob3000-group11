package no.usn.mob3000.domain.usecase.auth

import android.util.Log
import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.model.auth.UserDto
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource
import no.usn.mob3000.domain.model.auth.User
import no.usn.mob3000.domain.viewmodel.auth.AuthenticationViewModel

/**
 * Android Use Case for handling login operations.
 *
 * This encapsulates the business logic for the login process, and serves as
 * a bridge between the UI and Data layers.
 *
 * @property authRepository The repository handling authentication operations.
 * @property authenticationViewModel The authentication status view model.
 * @author frigvid
 * @created 2024-10-22
 */
class LoginUseCase(
    private val authRepository: AuthRepository = AuthRepository(AuthDataSource(), UserDataSource()),
    private val authenticationViewModel: AuthenticationViewModel = AuthenticationViewModel()
) {
    /**
     * Executes the login operation with the provided credentials.
     *
     * @param email The user's e-mail address.
     * @param password The user's password.
     * @return A [Result] containing either the [UserDto] data on success or an error on failure.
     * @author frigvid
     * @created 2024-10-22
     */
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<User> {
        if (!email.isValidEmail()) {
            return Result.failure(IllegalArgumentException("Invalid email format"))
        }

        /**
         * TODO: This is likely not necessary. Supabase has ways of controlling password strength,
         *       but I'll need to investigate how the current implementation reacts with this gone.
         *       I'm rather certain it'll simply work straight outta the gate, but doesn't hurt to
         *       try.
         */
        if (password.length < 8) {
            return Result.failure(IllegalArgumentException("Password must be at least 8 characters"))
        }

        return authRepository.login(email, password).onSuccess {
            Log.d("LoginUseCase", "Starting authentication state job.")
            authenticationViewModel.updateAuthState()
            authenticationViewModel.startPeriodicUpdates()
        }
    }


    private fun String.isValidEmail(): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}
