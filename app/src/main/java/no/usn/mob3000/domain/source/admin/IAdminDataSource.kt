package no.usn.mob3000.domain.source.admin

import no.usn.mob3000.data.model.auth.UserDto

/**
 * Administrator data source interface.
 *
 * @author frigvid
 * @created 2024-11-17
 */
interface IAdminDataSource {
    suspend fun promoteToAdmin(userId: String)
    suspend fun demoteToRegularUser(userId: String)
    suspend fun deleteUser(userId: String)
    suspend fun fetchAllUsers(): List<UserDto>
}
