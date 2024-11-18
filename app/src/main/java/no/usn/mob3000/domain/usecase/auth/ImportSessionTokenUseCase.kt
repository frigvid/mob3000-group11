package no.usn.mob3000.domain.usecase.auth

import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.data.source.remote.auth.AuthDataSource
import no.usn.mob3000.data.source.remote.auth.UserDataSource

/**
 * Android use case for importing a authentication session token into the Supabase client.
 *
 * @author Anarox
 * @created 2024-11-18
 */
class ImportSessionTokenUseCase(
    private val authRepository: AuthRepository = AuthRepository(AuthDataSource(), UserDataSource()),
    private val logoutUseCase: LogoutUseCase = LogoutUseCase()
) {
    /**
     * Imports a session token into supabase.
     *
     * @param token The authentication session token.
     * @author Anarox
     * @created 2024-10-22
     */
    suspend operator fun invoke(
        token: String
    ): Result<Unit> = try {
        logoutUseCase()
        authRepository.importSessionToken(token)
        Result.success(Unit)
    } catch (error: Exception) {
        Result.failure(error)
    }
}
