package no.usn.mob3000.domain.usecase.admin

import no.usn.mob3000.domain.source.admin.IAdminDataSource

/**
 * Administrator Android use case for promoting a regular user to an administrator.
 *
 * @author frigvid
 * @created 2024-11-17
 */
class PromoteToAdminUseCase(
    private val authDataSource: IAdminDataSource
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        return try {
            authDataSource.promoteToAdmin(userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
