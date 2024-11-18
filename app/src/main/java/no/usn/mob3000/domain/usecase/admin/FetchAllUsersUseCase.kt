package no.usn.mob3000.domain.usecase.admin

import no.usn.mob3000.data.repository.auth.AuthRepository
import no.usn.mob3000.domain.model.auth.User
import no.usn.mob3000.domain.source.admin.IAdminDataSource

/**
 * Administrator Android use case for fetching all users.
 *
 * @param authDataSource The data source to fetch users from.
 * @param authRepository The repository to map DTOs to domain objects.
 * @author frigvid
 * @created 2024-11-17
 */
class FetchAllUsersUseCase(
    private val authDataSource: IAdminDataSource,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return try {
            val userDtoList = authDataSource.fetchAllUsers()
            val users = userDtoList.map { dto -> authRepository.mapToDomainUser<User>(dto) }
            Result.success(users)
        } catch (error: Exception) {
            Result.failure(error)
        }
    }
}
