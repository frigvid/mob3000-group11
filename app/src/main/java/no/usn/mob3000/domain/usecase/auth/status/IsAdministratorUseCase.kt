package no.usn.mob3000.domain.usecase.auth.status

import no.usn.mob3000.data.source.remote.auth.AuthDataSource

/**
 * Check whether the user is an administrator.
 *
 * TODO: Not being an administrator should NOT impede login.
 *
 * @author frigvid
 * @created 2024-11-07
 */
class IsAdministratorUseCase(
    private val authDataSource: AuthDataSource = AuthDataSource()
) {
    suspend operator fun invoke(): Result<Boolean> = try {
        Result.success(authDataSource.checkAdminStatus())
    } catch (error: Exception) {
        Result.failure(error)
    }
}
