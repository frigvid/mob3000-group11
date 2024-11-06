package no.usn.mob3000.domain.usecase.auth

import no.usn.mob3000.data.source.remote.auth.AuthDataSource

/**
 * Use case for checking if the current user is an admin.
 *
 * @param authDataSource The data source for authentication-related operations.
 * @return Boolean indicating if the user is an admin.
 *
 * @author 258030
 * @created 2024-11-06
 */
class CheckAdminStatusUseCase(
    private val authDataSource: AuthDataSource = AuthDataSource()
) {
    suspend fun execute(): Boolean {
        return try {
            val currentUser = authDataSource.getCurrentUser()
            return authDataSource.checkAdminStatus(currentUser.id)
        } catch (e: Exception) {
            false
        }
    }
}




