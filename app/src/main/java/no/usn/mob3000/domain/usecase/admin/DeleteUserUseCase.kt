package no.usn.mob3000.domain.usecase.admin

import no.usn.mob3000.domain.source.admin.IAdminDataSource

/**
 * Administrator Android use case for deleting a user.
 *
 * @author frigvid
 * @created 2024-11-17
 */
class DeleteUserUseCase(
    private val authDataSource: IAdminDataSource
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        return try {
            authDataSource.deleteUser(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
